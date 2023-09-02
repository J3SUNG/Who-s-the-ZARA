package com.chibbol.wtz.global.timer.service;

import com.chibbol.wtz.domain.job.entity.RoomUserJob;
import com.chibbol.wtz.domain.job.entity.UserAbilityLog;
import com.chibbol.wtz.domain.job.entity.UserAbilityRecord;
import com.chibbol.wtz.domain.job.repository.RoomUserJobRedisRepository;
import com.chibbol.wtz.domain.job.repository.UserAbilityLogRepository;
import com.chibbol.wtz.domain.job.repository.UserAbilityRecordRedisRepository;
import com.chibbol.wtz.domain.job.service.JobService;
import com.chibbol.wtz.domain.level.service.UserLevelService;
import com.chibbol.wtz.domain.point.service.PointService;
import com.chibbol.wtz.domain.room.dto.CurrentSeatsDTO;
import com.chibbol.wtz.domain.room.entity.Room;
import com.chibbol.wtz.domain.room.repository.GameRepository;
import com.chibbol.wtz.domain.room.service.RoomEnterInfoRedisService;
import com.chibbol.wtz.domain.shop.entity.UserItem;
import com.chibbol.wtz.domain.shop.repository.UserItemRepository;
import com.chibbol.wtz.domain.user.repository.UserRepository;
import com.chibbol.wtz.domain.vote.service.VoteService;
import com.chibbol.wtz.global.timer.dto.*;
import com.chibbol.wtz.global.timer.entity.Timer;
import com.chibbol.wtz.global.timer.repository.TimerRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {
    private final int DAY_TIME = 90;
    private final int VOTE_TIME = 15;
    private final int VOTE_RESULT_TIME = 3;
    private final int NIGHT_TIME = 15;
    private final int NIGHT_RESULT_TIME = 8;

    private final JobService jobService;
    private final VoteService voteService;
    private final PointService pointService;
    private final UserLevelService userLevelService;
    private final StompTimerService stompTimerService;
    private final RoomEnterInfoRedisService roomEnterInfoRedisService;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserItemRepository userItemRepository;
    private final TimerRedisRepository timerRedisRepository;
    private final RoomUserJobRedisRepository roomUserJobRedisRepository;
    private final UserAbilityLogRepository userAbilityLogRepository;
    private final UserAbilityRecordRedisRepository userAbilityRecordRedisRepository;

    // 타이머 생성
    public Timer createRoomTimer(String gameCode) {
        timerRedisRepository.createGameTimer(gameCode);

        Room room = gameRepository.findRoomByGameCode(gameCode);

        List<CurrentSeatsDTO> currentSeatsDTOs = roomEnterInfoRedisService.getUserEnterInfo(room.getRoomCode());
        for(CurrentSeatsDTO currentSeatsDTO : currentSeatsDTOs) {
            if(currentSeatsDTO.getUserSeq() <= 0) {
                continue;
            }
            roomUserJobRedisRepository.save(RoomUserJob.builder().userSeq(currentSeatsDTO.getUserSeq()).gameCode(gameCode).build());
        }

        return timerRedisRepository.getGameTimerInfo(gameCode);
    }

    // 해당 방 타이머 정보 조회
    public Timer getTimerInfo(String gameCode) {
        return timerRedisRepository.getGameTimerInfo(gameCode);
    }

    // 해당 유저의 타이머 끝남을 저장
    public synchronized void timerEndUser(String gameCode, Long userSeq) {
        Timer timer = timerRedisRepository.getGameTimerInfo(gameCode);

        if (timer == null) {
            return;
        }

        if(timer.getTimerEndUserSeqs().contains(userSeq)) {
            return;
        }

        timer.getTimerEndUserSeqs().add(userSeq);
        timerRedisRepository.updateTimer(gameCode, timer);

        checkTimerEnd(gameCode, timer);
    }

    // 방에 있는 모든 유저의 타이머 끝남을 확인
    private synchronized void checkTimerEnd(String gameCode, Timer timer) {
        Room room = gameRepository.findRoomByGameCode(gameCode);

        List<CurrentSeatsDTO> currentSeatsDTOs = roomEnterInfoRedisService.getUserEnterInfo(room.getRoomCode());
        List<Long> enterUsers = currentSeatsDTOs.stream()
                .map(CurrentSeatsDTO::getUserSeq)
                .filter(userSeq -> userSeq > 0) // 빈자리 0을 필터링하여 제외
                .collect(Collectors.toList());

        if (timer.getTimerEndUserSeqs().containsAll(enterUsers)) {
            timerTypeChange(gameCode);
        }
    }


    public synchronized void timerDecreaseUser(String gameCode, TimerDecreaseDTO timerDecreaseDTO) {
        Long userSeq = timerDecreaseDTO.getUserSeq();
        int decreaseTime = timerDecreaseDTO.getDecreaseTime();

        Timer timer = timerRedisRepository.getGameTimerInfo(gameCode);

        if(timer == null) {
            return;
        }

        // 낮시간에만 시간을 줄일 수 있음
        if(!timer.getTimerType().equals("DAY")) {
            return;
        }

        // 죽은 사람이 요청을 보냈을 때
        if(!roomUserJobRedisRepository.findByGameCodeAndUserSeq(gameCode, userSeq).isAlive()) {
            return;
        }

        // 이미 요청을 보낸 사람일 때
        if(timer.getTimerDecreaseUserSeqs().contains(userSeq)) {
            return;
        }

        timer.getTimerDecreaseUserSeqs().add(userSeq);
        timerRedisRepository.updateTimer(gameCode, timer);
        stompTimerService.sendToClient("GAME_TIMER_DECREASE", gameCode, decreaseTime);
    }

//    타이머 타입 변경 로직 시작


    public synchronized void timerTypeChange(String gameCode) {
        Timer timer = timerRedisRepository.getGameTimerInfo(gameCode);

        String currentType = timer.getTimerType();
        log.info("timerTypeChange before : " + currentType);

        switch (currentType) {
            case "NONE":
                startNewDay(gameCode);
                break;
            case "DAY":
                switchToVotePhase(timer, gameCode);
                break;
            case "VOTE":
                checkVoteResult(timer, gameCode);
                break;
            case "VOTE_RESULT":
                switchToNightPhase(timer, gameCode);
                break;
            case "NIGHT":
                checkNightResult(timer, gameCode);
                break;
            case "NIGHT_RESULT":
                switchToNextDay(timer, gameCode);
                break;
            default:
                break;
        }

        log.info("timerTypeChange after : " + timer.getTimerType());
    }

    private void handleTimerChangeError(Timer timer, String gameCode) {
        timerRedisRepository.updateTimer(gameCode, timer.update(Timer.builder().build()));
        logTimerChangeError(timer, gameCode);
    }

    private void startNewDay(String gameCode) {
        Timer timer = Timer.builder()
                .timerType("DAY")
                .remainingTime(DAY_TIME)
                .turn(1)
                .startAt(LocalDateTime.now())
                .build();

        timerRedisRepository.updateTimer(gameCode, timer);

        List<RoomUserJob> roomUserRandomJob = jobService.randomJobInGameUser(gameCode);

        // 직업 정보, 게임 시작 알림
        stompTimerService.sendToClient("GAME_START", gameCode, roomUserJobsToData(roomUserRandomJob));
        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());
        stompTimerService.sendToClient("GAME_BLACKOUT", gameCode, generateBlackOutData(gameCode));

        logTimerTypeChange(gameCode, "NONE", "DAY");
    }

    private void switchToVotePhase(Timer timer, String gameCode) {
        timer.update(Timer.builder().timerType("VOTE").remainingTime(VOTE_TIME).startAt(LocalDateTime.now()).build());
        timerRedisRepository.updateTimer(gameCode, timer);

        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());

        logTimerTypeChange(gameCode, "DAY", "VOTE");
    }

    private void checkVoteResult(Timer timer, String gameCode) {
        VoteResultDataDTO voteResultData = voteService.voteResult(gameCode, timer.getTurn());
        stompTimerService.sendToClient("GAME_VOTE_RESULT", gameCode, voteResultData);

        List<UserAbilityLog> userAbilityLogs = jobService.checkGameOver(gameCode);
        if(userAbilityLogs != null) {
            switchToGameOver(userAbilityLogs, gameCode);
        } else {
            switchToVoteResultPhase(timer, gameCode);
        }
    }

    private void switchToVoteResultPhase(Timer timer, String gameCode) {
        timer.update(Timer.builder().timerType("VOTE_RESULT").remainingTime(VOTE_RESULT_TIME).startAt(LocalDateTime.now()).build());
        timerRedisRepository.updateTimer(gameCode, timer);
        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());

        logTimerTypeChange(gameCode, "VOTE", "VOTE_RESULT");
    }

    private void switchToNightPhase(Timer timer, String gameCode) {
        timer.update(Timer.builder().timerType("NIGHT").remainingTime(NIGHT_TIME).startAt(LocalDateTime.now()).build());
        timerRedisRepository.updateTimer(gameCode, timer);
        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());

        logTimerTypeChange(gameCode, "VOTE_RESULT", "NIGHT");
    }

    private void checkNightResult(Timer timer, String gameCode) {
        Map<String, Long> turnResult = jobService.useAbilityNight(gameCode, timer.getTurn());
        List<UserAbilityRecord> userAbilityRecords = userAbilityRecordRedisRepository.findAllByGameCodeAndTurn(gameCode, timer.getTurn());
        List<RoomUserJob> roomUser = roomUserJobRedisRepository.findAllByGameCode(gameCode);

        stompTimerService.sendToClient("GAME_NIGHT_RESULT", gameCode, userAbilityLogsToData(turnResult, userAbilityRecords, roomUser));

        // 게임 끝났으면 GAME_OVER, 아니면 NIGHT_RESULT
        List<UserAbilityLog> userAbilityLogs = jobService.checkGameOver(gameCode);
        if(userAbilityLogs != null) {
            switchToGameOver(userAbilityLogs, gameCode);
        } else {
            switchToNightResultPhase(timer, gameCode);
        }
    }

    private void switchToNightResultPhase(Timer timer, String gameCode) {
        timer.update(Timer.builder().timerType("NIGHT_RESULT").remainingTime(NIGHT_RESULT_TIME).startAt(LocalDateTime.now()).build());
        timerRedisRepository.updateTimer(gameCode, timer);
        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());

        logTimerTypeChange(gameCode, "NIGHT", "NIGHT_RESULT");
    }

    private void switchToNextDay(Timer timer, String gameCode) {
        timer.update(Timer.builder().timerType("DAY").remainingTime(DAY_TIME).turn(timer.getTurn() + 1).startAt(LocalDateTime.now()).build());
        timerRedisRepository.updateTimer(gameCode, timer);
        stompTimerService.sendToClient("GAME_TIMER", gameCode, TimerDTO.builder().type(timer.getTimerType()).time(timer.getRemainingTime()).build());
        stompTimerService.sendToClient("GAME_BLACKOUT", gameCode, generateBlackOutData(gameCode));

        logTimerTypeChange(gameCode, "NIGHT_RESULT", "DAY");
    }

    private void switchToGameOver(List<UserAbilityLog> userAbilityLogs, String gameCode) {
        giveExpAndPoint(userAbilityLogs);
        stompTimerService.sendToClient("GAME_OVER", gameCode, userAbilityLogsToData(userAbilityLogs));
        timerRedisRepository.deleteGameTimer(gameCode);

        logGameOver(gameCode);
    }

//    타이머 타입 변경 로직 끝


//    블랙아웃 로직 시작

    private BlackOutDataDTO generateBlackOutData(String gameCode) {
        List<RoomUserJob> aliveRoomUsers = roomUserJobRedisRepository.findAllByGameCode(gameCode)
                .stream()
                .filter(RoomUserJob::isAlive)
                .collect(Collectors.toList());

        if (!aliveRoomUsers.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(aliveRoomUsers.size());
            RoomUserJob randomUser = aliveRoomUsers.get(randomIndex);

            int startSecond = 40; // 기본 값

            logBlackOutInfo(gameCode, randomUser.getUserSeq(), startSecond);

            return BlackOutDataDTO.builder()
                    .userSeq(randomUser.getUserSeq())
                    .startSecond(startSecond)
                    .build();
        }

        return BlackOutDataDTO.builder().build();
    }

    private List<GameUserDataDTO> roomUserJobsToData(List<RoomUserJob> roomUserJobs) {
        List<GameUserDataDTO> gameUserDataDTOList = roomUserJobs.stream()
                .map(this::createGameUserDataDTO)
                .collect(Collectors.toList());

        gameUserDataDTOList.forEach(this::populateEquippedItems);

        return gameUserDataDTOList;
    }

    private GameUserDataDTO createGameUserDataDTO(RoomUserJob roomUserJob) {
        return GameUserDataDTO.builder()
                .userSeq(roomUserJob.getUserSeq())
                .jobSeq(roomUserJob.getJobSeq())
                .nickname(userRepository.findNicknameByUserSeq(roomUserJob.getUserSeq()))
                .build();
    }

    private void populateEquippedItems(GameUserDataDTO gameUserDataDTO) {
        Map<String, byte[]> equippedItems = gameUserDataDTO.getEquippedItems();
        Map<String, byte[]> equippedItemsGif = gameUserDataDTO.getEquippedItemsGif();

        equippedItems.keySet().forEach(type -> {
            equippedItems.put(type, getEquippedItem(gameUserDataDTO.getUserSeq(), type));
            equippedItemsGif.put(type, getEquippedItemGif(gameUserDataDTO.getUserSeq(), type));
        });
    }

    private GameResultDataDTO userAbilityLogsToData(List<UserAbilityLog> userAbilityLogs) {
        if (userAbilityLogs.isEmpty()) {
            return null;
        }

        List<GameResultDataDTO.GameResult> gameResults = userAbilityLogs.stream()
                .map(this::createGameResult)
                .collect(Collectors.toList());

        return GameResultDataDTO.builder()
                .rabbitWin(isRabbitWin(userAbilityLogs.get(0).getGameCode()))
                .userInfo(gameResults)
                .build();
    }

    private GameResultDataDTO.GameResult createGameResult(UserAbilityLog userAbilityLog) {
        return GameResultDataDTO.GameResult.builder()
                .userSeq(userAbilityLog.getUser().getUserSeq())
                .jobSeq(userAbilityLog.getJob().getJobSeq())
                .nickname(userAbilityLog.getUser().getNickname())
                .win(userAbilityLog.isResult())
                .build();
    }

    private NightResultDataDTO userAbilityLogsToData(Map<String, Long> turnResult, List<UserAbilityRecord> userAbilityLogs, List<RoomUserJob> roomUserJobs) {
        NightResultDataDTO nightResultDataDTO = NightResultDataDTO.builder()
                .deadUserSeq(turnResult.get("kill"))
                .threatUserSeq(turnResult.get("Gangster"))
                .healUserSeq(turnResult.get("Doctor"))
                .build();

        roomUserJobs.forEach(roomUserJob -> nightResultDataDTO.addAbility(roomUserJob.getUserSeq(), null, false));
        userAbilityLogs.forEach(userAbilityRecord -> nightResultDataDTO.addAbility(userAbilityRecord.getUserSeq(), userAbilityRecord.getTargetUserSeq(), userAbilityRecord.isSuccess()));

        return nightResultDataDTO;
    }


    private boolean isRabbitWin(String gameCode) {
        List<UserAbilityLog> userAbilityLogs = userAbilityLogRepository.findAllByGameCode(gameCode);

        Long mafiaSeq = jobService.getMafiaSeq();
        for (UserAbilityLog userAbilityLog : userAbilityLogs) {
            if(userAbilityLog.getJob().getJobSeq().equals(mafiaSeq)) {
                return !userAbilityLog.isResult();
            }
        }

        log.info("isRabbitWin error");
        return false;
    }

    private void giveExpAndPoint(List<UserAbilityLog> userAbilityLogs) {
        userLevelService.updateExp(userAbilityLogs);
        pointService.updatePoint(userAbilityLogs);
    }

    private byte[] getEquippedItem(Long userSeq, String type) {
        String IMAGE_PATH = "static/item_images/";
        UserItem item = userItemRepository.findByUserUserSeqAndItemTypeAndEquipped(userSeq, type, true);

        byte[] imageData = null;
        try {
            Path imageFilePath = null;
            if(item == null) {
                // 이미지를 byte[]로 변환
                imageFilePath = Paths.get(IMAGE_PATH + type).resolve("none.png");
            } else {
                // 이미지를 byte[]로 변환
                imageFilePath = Paths.get(IMAGE_PATH + item.getItem().getType()).resolve(item.getItem().getImage());

            }
            Resource resource = new ClassPathResource(imageFilePath.toString());
            imageData = resource.getInputStream().readAllBytes();

            return imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private byte[] getEquippedItemGif(Long userSeq, String type) {
        String GIF_PATH = "static/item_gifs/";
        UserItem item = userItemRepository.findByUserUserSeqAndItemTypeAndEquipped(userSeq, type, true);

        byte[] imageData = null;
        try {
            Path imageFilePath = null;
            if(item == null) {
                // 이미지를 byte[]로 변환
                imageFilePath = Paths.get(GIF_PATH + type).resolve("none.png");
            } else {
                // 이미지를 byte[]로 변환
                imageFilePath = Paths.get(GIF_PATH + item.getItem().getType()).resolve(item.getItem().getGif());

            }
            Resource resource = new ClassPathResource(imageFilePath.toString());
            imageData = resource.getInputStream().readAllBytes();

            return imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    private void logTimerChangeError(Timer timer, String gameCode) {
        log.warn("====================================");
        log.warn("TIMER CHANGE ERROR");
        log.warn("timer : " + timer);
        log.warn("gameCode : " + gameCode);
        log.warn("====================================");
    }

    private void logTimerTypeChange(String gameCode, String from, String to) {
        log.info("====================================");
        log.info("TIMER TYPE CHANGE");
        log.info("gameCode : " + gameCode);
        log.info("from : " + from);
        log.info("to : " + to);
        log.info("====================================");
    }

    private void logGameOver(String gameCode) {
        log.info("====================================");
        log.info("GAME OVER");
        log.info("gameCode : " + gameCode);
        log.info("====================================");
    }

    private void logBlackOutInfo(String gameCode, Long userSeq, int blackOutTime) {
        log.info("====================================");
        log.info("BLACK OUT");
        log.info("gameCode : " + gameCode);
        log.info("userSeq : " + userSeq);
        log.info("blackOutTime : " + blackOutTime);
        log.info("====================================");
    }
}
