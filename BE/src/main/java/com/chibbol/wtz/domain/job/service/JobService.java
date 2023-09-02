package com.chibbol.wtz.domain.job.service;

import com.chibbol.wtz.domain.job.entity.*;
import com.chibbol.wtz.domain.job.exception.UserJobNotExistsException;
import com.chibbol.wtz.domain.job.repository.*;
import com.chibbol.wtz.domain.job.type.*;
import com.chibbol.wtz.domain.room.entity.Game;
import com.chibbol.wtz.domain.room.repository.GameRepository;
import com.chibbol.wtz.domain.room.repository.RoomJobSettingRedisRepository;
import com.chibbol.wtz.domain.user.repository.UserRepository;
import com.chibbol.wtz.domain.vote.entity.VoteTurnRecord;
import com.chibbol.wtz.domain.vote.repository.VoteRedisRepository;
import com.chibbol.wtz.domain.vote.repository.VoteTurnRecordRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Service
public class JobService {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RoomUserJobRedisRepository roomUserJobRedisRepository;
    private final UserAbilityLogRepository userAbilityLogRepository;

    private final VoteRedisRepository voteRedisRepository;
    private final GameRepository gameRepository;
    private final RoomJobSettingRedisRepository roomJobSettingRedisRepository;
    private final VoteTurnRecordRepository voteTurnRecordRepository;
    private final UserAbilityRecordRedisRepository userAbilityRecordRedisRepository;
    private final UserAbilityTurnRecordRepository userAbilityTurnRecordRepository;

    private final Map<Long, Job> jobMap;
    private final Long mafiaSeq;

    public JobService(JobRepository jobRepository, UserRepository userRepository, RoomUserJobRedisRepository roomUserJobRedisRepository, UserAbilityLogRepository userAbilityLogRepository, VoteRedisRepository voteRedisRepository, GameRepository gameRepository, RoomJobSettingRedisRepository roomJobSettingRedisRepository, VoteTurnRecordRepository voteTurnRecordRepository, UserAbilityRecordRedisRepository userAbilityRecordRedisRepository, UserAbilityTurnRecordRepository userAbilityTurnRecordRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.roomUserJobRedisRepository = roomUserJobRedisRepository;
        this.userAbilityLogRepository = userAbilityLogRepository;
        this.voteRedisRepository = voteRedisRepository;
        this.gameRepository = gameRepository;
        this.roomJobSettingRedisRepository = roomJobSettingRedisRepository;
        this.voteTurnRecordRepository = voteTurnRecordRepository;
        this.userAbilityRecordRedisRepository = userAbilityRecordRedisRepository;

        // jobRepository.findAll()을 사용하여 jobMap 초기화
        this.jobMap = jobRepository.findAll().stream().collect(Collectors.toMap(Job::getJobSeq, job -> job));
        this.mafiaSeq = jobRepository.findByName("Mafia").getJobSeq();
        this.userAbilityTurnRecordRepository = userAbilityTurnRecordRepository;
    }

//    **********  직업 랜덤 배정 로직 시작  **********

    public List<RoomUserJob> randomJobInGameUser(String gameCode) {
        List<RoomUserJob> joinUser = roomUserJobRedisRepository.findAllByGameCode(gameCode);

        List<Long> excludeJobSeq = roomJobSettingRedisRepository.findExcludeJobSeqByGameCode(gameCode);
        Job mafia = jobRepository.findByName("Mafia");
        Integer mafiaCount = (joinUser.size() >= 8) ? 2 : 1; // Integer로 변경

        Collections.shuffle(joinUser);

        for (RoomUserJob roomUserJob : joinUser) {
            // mafiaCount 값을 메서드 호출 후 반환값으로 업데이트
            mafiaCount = assignRandomJob(gameCode, roomUserJob, excludeJobSeq, mafia, mafiaCount);
        }

        logRandomJobAssignment(gameCode, joinUser);

        return roomUserJobRedisRepository.findAllByGameCode(gameCode);
    }

    private Integer assignRandomJob(String gameCode, RoomUserJob roomUserJob, List<Long> excludeJobSeq, Job mafia, Integer mafiaCount) {
        List<Job> availableJobs = getAvailableJobs(excludeJobSeq, mafia, mafiaCount);

        // 랜덤 직업 배정
        Collections.shuffle(availableJobs);
        Job job = availableJobs.get(0);

        // 배정한 직업 재배정하지 않기 위해 제외 직업에 추가
        if (job.getJobSeq() != 1) {
            excludeJobSeq.add(job.getJobSeq());
        }

        // 유저 직업 저장
        roomUserJobRedisRepository.save(RoomUserJob.builder()
                .gameCode(gameCode)
                .jobSeq(job.getJobSeq())
                .userSeq(roomUserJob.getUserSeq())
                .canVote(true)
                .isAlive(true)
                .build());

        return mafiaCount - 1; // 값 감소 후 반환
    }

    // 사용 가능한 직업 목록 조회
    private List<Job> getAvailableJobs(List<Long> excludeJobSeq, Job mafia, int mafiaCount) {
        List<Job> jobs = jobRepository.findAll();
        List<Job> availableJobs = new ArrayList<>(jobs);
        availableJobs.removeIf(job -> excludeJobSeq.contains(job.getJobSeq()));

        // 마피아 우선 배정
        if (mafiaCount > 0 && mafia != null) {
            availableJobs.clear();
            availableJobs.add(mafia);
        }

        return availableJobs;
    }


//    **********  직업 랜덤 배정 로직 끝  **********


//    **********  밤 능력 사용 로직 시작  **********

    public Map<String, Long> useAbilityNight(String gameCode, int turn) {
        List<RoomUserJob> roomUsers = roomUserJobRedisRepository.findAllByGameCode(gameCode);
        List<UserAbilityRecord> userAbilityRecords = getUserAbilityRecordsByGameAndTurn(gameCode, turn);

        Map<String, Long> turnResult = new HashMap<>();

        // 군인 능력
        Long soldierSeq = jobRepository.findByName("Soldier").getJobSeq();
        addSoldierAbilityRecords(gameCode, turn, roomUsers, userAbilityRecords, soldierSeq);

        // 능력 사용 순서 정하기
        List<JobInterface> jobAbility = getSortedJobAbilities(userAbilityRecords);

        // 능력 사용
        executeJobAbilities(gameCode, jobAbility, turnResult);

        saveTurnResult(gameCode, turnResult, userAbilityRecords);

        logUseAbilityNight(gameCode, turn, turnResult);

        return turnResult;
    }

    // redis에서 roomSeq, turn에 사용한 능력 조회
    public List<UserAbilityRecord> getUserAbilityRecordsByGameAndTurn(String gameCode, int turn) {
        return userAbilityRecordRedisRepository.findAllByGameCodeAndTurn(gameCode, turn);
    }

    private void addSoldierAbilityRecords(String gameCode, int turn, List<RoomUserJob> roomUsers, List<UserAbilityRecord> userAbilityRecords, Long soldierSeq) {
        for (RoomUserJob roomUser : roomUsers) {
            if (roomUser.getJobSeq().equals(soldierSeq) && roomUser.isAlive()) {
                userAbilityRecords.add(UserAbilityRecord.builder()
                        .gameCode(gameCode)
                        .turn(turn)
                        .userSeq(roomUser.getUserSeq())
                        .targetUserSeq(roomUser.getUserSeq())
                        .build());
            }
        }
    }

    private List<JobInterface> getSortedJobAbilities(List<UserAbilityRecord> userAbilityRecords) {
        return userAbilityRecords.stream()
                .map(this::matchJobNight)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(JobInterface::getWeight))
                .collect(Collectors.toList());
    }

    private void executeJobAbilities(String gameCode, List<JobInterface> jobAbility, Map<String, Long> turnResult) {
        for (JobInterface jobInterface : jobAbility) {
            if (roomUserJobRedisRepository.findByGameCodeAndUserSeq(gameCode, jobInterface.getUserSeq()).isAlive()) {
                jobInterface.useAbility(turnResult);
            }
        }
    }

//    **********  밤 능력 사용 로직 끝  **********



//    **********  밤 직업 매핑 로직 시작  **********

    public JobInterface matchJobNight(UserAbilityRecord userAbilityRecord) {
        Long userSeq = userAbilityRecord.getUserSeq();
        String gameCode = userAbilityRecord.getGameCode();
        Long targetUserSeq = userAbilityRecord.getTargetUserSeq();
        LocalDateTime useTime = userAbilityRecord.getUsedAt();

        RoomUserJob roomUserJob = roomUserJobRedisRepository.findByGameCodeAndUserSeq(gameCode, userSeq);

        if (roomUserJob == null) {
            throw new UserJobNotExistsException("유저 직업 정보가 없습니다.");
        }

        if (!roomUserJob.isAlive()) {
            return null;
        }

        return createJobInstance(roomUserJob, userSeq, targetUserSeq, useTime);
    }

    private JobInterface createJobInstance(RoomUserJob roomUserJob, Long userSeq, Long targetUserSeq, LocalDateTime useTime) {
        String jobName = jobMap.get(roomUserJob.getJobSeq()).getName();

        switch (jobName) {
            case "Doctor":
                return Doctor.builder().userSeq(userSeq).targetUserSeq(targetUserSeq).build();
            case "Police":
                return Police.builder().userSeq(userSeq).targetUserSeq(targetUserSeq).build();
            case "Gangster":
                return Gangster.builder().userSeq(userSeq).targetUserSeq(targetUserSeq).build();
            case "Soldier":
                return Soldier.builder().userSeq(userSeq).targetUserSeq(targetUserSeq).build();
            case "Mafia":
                return Mafia.builder().userSeq(userSeq).targetUserSeq(targetUserSeq).useTime(useTime).build();
            default:
                return null;
        }
    }

//    **********  밤 직업 매핑 로직 끝  **********



//    **********  밤 능력 결과 확인, 저장 로직 시작  **********

    public void saveTurnResult(String gameCode, Map<String, Long> turnResult, List<UserAbilityRecord> userAbilityRecords) {
        Map<Long, RoomUserJob> userJobs = getUserJobsForUserSeqs(gameCode, userAbilityRecords);
        List<RoomUserJob> jobsToUpdate = new ArrayList<>();
        List<UserAbilityRecord> recordsToSave = new ArrayList<>();

        processSoldierAbility(turnResult, userJobs);

        for (UserAbilityRecord userAbilityRecord : userAbilityRecords) {
            Long userSeq = userAbilityRecord.getUserSeq();
            RoomUserJob userJob = userJobs.get(userSeq);

            if (userJob != null) {
                String jobName = jobMap.get(userJob.getJobSeq()).getName();

                processAbilityResult(turnResult, userJob, userAbilityRecord, jobName, recordsToSave, jobsToUpdate);
            }
        }

        batchUpdateJobsAndRecords(jobsToUpdate, recordsToSave);
    }

    private Map<Long, RoomUserJob> getUserJobsForUserSeqs(String gameCode, List<UserAbilityRecord> userAbilityRecords) {
        Set<Long> userSeqs = userAbilityRecords.stream()
                .map(UserAbilityRecord::getUserSeq)
                .collect(Collectors.toSet());

        Map<Long, RoomUserJob> userJobs = new HashMap<>();

        for (Long userSeq : userSeqs) {
            userJobs.put(userSeq, roomUserJobRedisRepository.findByGameCodeAndUserSeq(gameCode, userSeq));
        }

        return userJobs;
    }

    private void processSoldierAbility(Map<String, Long> turnResult, Map<Long, RoomUserJob> userJobs) {
        if (turnResult.containsKey("Soldier")) {
            Long userSeq = turnResult.get("Soldier");
            RoomUserJob userJob = userJobs.get(userSeq);
            if (userJob != null && userJob.isUseAbility()) {
                turnResult.remove("Soldier");
                turnResult.put("kill", userSeq);
            }
        }
    }

    private void processAbilityResult(Map<String, Long> turnResult, RoomUserJob userJob, UserAbilityRecord userAbilityRecord, String jobName, List<UserAbilityRecord> recordsToSave, List<RoomUserJob> jobsToUpdate) {
        switch (jobName) {
            case "Doctor":
                processDoctorAbility(turnResult, userAbilityRecord, recordsToSave);
                break;
            case "Police":
                processPoliceAbility(turnResult, userAbilityRecord, recordsToSave);
                break;
            case "Gangster":
                processGangsterAbility(turnResult, userAbilityRecord, recordsToSave, jobsToUpdate);
                break;
            case "Soldier":
                processSoldierAbility(turnResult, userJob, userAbilityRecord, recordsToSave, jobsToUpdate);
                break;
            case "Mafia":
                processMafiaAbility(turnResult, userAbilityRecord, recordsToSave, jobsToUpdate);
                break;
            default:
                break;
        }
    }

    private void processDoctorAbility(Map<String, Long> turnResult, UserAbilityRecord userAbilityRecord, List<UserAbilityRecord> recordsToSave) {
        if (turnResult.containsKey("Doctor")) {
            recordsToSave.add(userAbilityRecord.success());
        }
    }

    private void processPoliceAbility(Map<String, Long> turnResult, UserAbilityRecord userAbilityRecord, List<UserAbilityRecord> recordsToSave) {
        if (turnResult.containsKey("Police")) {
            Long targetUserSeq = turnResult.get("Police");
            RoomUserJob targetUserJob = roomUserJobRedisRepository.findByGameCodeAndUserSeq(userAbilityRecord.getGameCode(), targetUserSeq);
            if (targetUserJob != null && targetUserJob.getJobSeq().equals(mafiaSeq)) {
                recordsToSave.add(userAbilityRecord.success());
            }
        }
    }

    private void processGangsterAbility(Map<String, Long> turnResult, UserAbilityRecord userAbilityRecord, List<UserAbilityRecord> recordsToSave, List<RoomUserJob> jobsToUpdate) {
        if (turnResult.containsKey("Gangster")) {
            RoomUserJob targetUserJob = roomUserJobRedisRepository.findByGameCodeAndUserSeq(userAbilityRecord.getGameCode(), turnResult.get("Gangster"));
            if (targetUserJob != null) {
                jobsToUpdate.add(targetUserJob.canVote(false));
                recordsToSave.add(userAbilityRecord.success());
            }
            if (targetUserJob != null && targetUserJob.getJobSeq().equals(mafiaSeq)) {
                recordsToSave.add(userAbilityRecord.success());
            }
        }
    }

    private void processSoldierAbility(Map<String, Long> turnResult, RoomUserJob userJob, UserAbilityRecord userAbilityRecord, List<UserAbilityRecord> recordsToSave, List<RoomUserJob> jobsToUpdate) {
        if (turnResult.containsKey("Soldier")) {
            jobsToUpdate.add(userJob.useAbility());
            recordsToSave.add(userAbilityRecord.success());
        }
    }

    private void processMafiaAbility(Map<String, Long> turnResult, UserAbilityRecord userAbilityRecord, List<UserAbilityRecord> recordsToSave, List<RoomUserJob> jobsToUpdate) {
        if (turnResult.containsKey("kill")) {
            RoomUserJob targetUserJob = roomUserJobRedisRepository.findByGameCodeAndUserSeq(userAbilityRecord.getGameCode(), turnResult.get("kill"));
            if (targetUserJob != null) {
                jobsToUpdate.add(targetUserJob.kill());
                recordsToSave.add(userAbilityRecord.success());
            }
        }
    }

    private void batchUpdateJobsAndRecords(List<RoomUserJob> jobsToUpdate, List<UserAbilityRecord> recordsToSave) {
        if (!jobsToUpdate.isEmpty()) {
            roomUserJobRedisRepository.saveAll(jobsToUpdate);
        }
        if (!recordsToSave.isEmpty()) {
            userAbilityRecordRedisRepository.saveAll(recordsToSave);
        }
    }

//    **********  밤 능력 결과 확인, 저장 로직 끝  **********


//    **********  게임 종료 여부 확인 로직 시작  **********

    public List<UserAbilityLog> checkGameOver(String gameCode) {
        List<RoomUserJob> roomUserJobs = roomUserJobRedisRepository.findAllByGameCode(gameCode);

        int mafiaCount = 0;
        int citizenCount = 0;

        for (RoomUserJob roomUserJob : roomUserJobs) {
            if (roomUserJob.isAlive()) {
                if (roomUserJob.getJobSeq().equals(mafiaSeq)) {
                    mafiaCount++;
                } else {
                    citizenCount++;
                }
            }
        }

        logGameStateInfo(roomUserJobs, citizenCount, mafiaCount);

        if (isMafiaVictory(mafiaCount)) {
            return saveUserAbilityRecord(gameCode, true);
        } else if (isCitizenVictory(mafiaCount, citizenCount)) {
            return saveUserAbilityRecord(gameCode, false);
        }

        return null;
    }

    private boolean isMafiaVictory(int mafiaCount) {
        return mafiaCount == 0;
    }

    private boolean isCitizenVictory(int mafiaCount, int citizenCount) {
        return mafiaCount >= citizenCount;
    }

//    **********  게임 종료 여부 확인 로직 끝  **********


//    **********  게임 결과 저장 로직 시작  **********

    public List<UserAbilityLog> saveUserAbilityRecord(String gameCode, boolean win) {
        List<UserAbilityRecord> userAbilityRecords = userAbilityRecordRedisRepository.findAllByGameCode(gameCode);

        Game game = gameRepository.findByGameCode(gameCode);
        game.endGame();
        gameRepository.save(game);

        Map<Long, UserAbilityLog> userAbilityLogs = prepareUserAbilityLogs(gameCode, win);
        updateAbilitySuccessCounts(userAbilityLogs, userAbilityRecords);

        saveLogsAndClearRedis(gameCode, userAbilityLogs, userAbilityRecords);
        saveVoteTurnRecords(gameCode);
        deleteRoomUserJobs(gameCode);

        logAbilityResult(gameCode);

        return new ArrayList<>(userAbilityLogs.values());
    }

    private Map<Long, UserAbilityLog> prepareUserAbilityLogs(String gameCode, boolean win) {
        Map<Long, UserAbilityLog> userAbilityLogs = new HashMap<>();
        Game game = gameRepository.findByGameCode(gameCode);

        List<RoomUserJob> roomUserJobList = roomUserJobRedisRepository.findAllByGameCode(gameCode);
        for (RoomUserJob roomUser : roomUserJobList) {
            Long userSeq = roomUser.getUserSeq();
            userAbilityLogs.put(userSeq, createUserAbilityLog(userSeq, game, roomUser, win));
        }

        return userAbilityLogs;
    }

    private UserAbilityLog createUserAbilityLog(Long userSeq, Game game, RoomUserJob roomUser, boolean win) {
        return UserAbilityLog.builder()
                .user(userRepository.findByUserSeq(userSeq))
                .gameCode(game.getGameCode())
                .job(jobMap.get(roomUser.getJobSeq()))
                .result(checkUserJobWin(roomUser.getJobSeq(), win))
                .abilitySuccessCount(0)
                .startAt(game.getStartAt())
                .endAt(game.getEndAt())
                .build();
    }

    private void updateAbilitySuccessCounts(Map<Long, UserAbilityLog> userAbilityLogs, List<UserAbilityRecord> userAbilityRecords) {
        for (UserAbilityRecord userAbilityRecord : userAbilityRecords) {
            Long userSeq = userAbilityRecord.getUserSeq();
            if (userAbilityRecord.isSuccess()) {
                UserAbilityLog userAbilityLog = userAbilityLogs.get(userSeq);
                userAbilityLog.addAbilitySuccessCount();
            }
        }
    }

    private void saveLogsAndClearRedis(String gameCode, Map<Long, UserAbilityLog> userAbilityLogs, List<UserAbilityRecord> userAbilityRecords) {
        userAbilityLogRepository.saveAll(userAbilityLogs.values());
        userAbilityTurnRecordRepository.saveAll(userAbilityRecords.stream()
                .map(userAbilityRecord -> UserAbilityTurnRecord.builder()
                        .gameCode(userAbilityRecord.getGameCode())
                        .userSeq(userAbilityRecord.getUserSeq())
                        .targetUserSeq(userAbilityRecord.getTargetUserSeq())
                        .success(userAbilityRecord.isSuccess())
                        .usedAt(userAbilityRecord.getUsedAt())
                        .build())
                .collect(Collectors.toList()));
        userAbilityRecordRedisRepository.deleteAllByGameCode(gameCode);
    }

    private void saveVoteTurnRecords(String gameCode) {
        voteTurnRecordRepository.saveAll(voteRedisRepository.findAllByGameCode(gameCode).stream()
                .map(vote -> VoteTurnRecord.builder()
                        .gameCode(vote.getGameCode())
                        .turn(vote.getTurn())
                        .userSeq(vote.getUserSeq())
                        .targetUserSeq(vote.getTargetUserSeq())
                        .build())
                .collect(Collectors.toList()));
    }

    private void deleteRoomUserJobs(String gameCode) {
        roomUserJobRedisRepository.deleteByGameCode(gameCode);
    }

    public boolean checkUserJobWin(Long jobSeq, boolean win) {
        return win != (mafiaSeq.equals(jobSeq));
    }

//    **********  게임 결과 저장 로직 끝  **********



    private void logRandomJobAssignment(String gameCode, List<RoomUserJob> joinUser) {
        log.info("=====================================");
        log.info("SUCCESS RANDOM JOB ASSIGN");
        log.info("GAME_CODE : " + gameCode);
        log.info("USER_SEQ : " + joinUser.stream().map(RoomUserJob::getUserSeq).collect(Collectors.toList()));
        log.info("EXCLUDE_JOB_SEQ : " + roomJobSettingRedisRepository.findExcludeJobSeqByGameCode(gameCode));
        log.info("=====================================");
    }

    private void logUseAbilityNight(String gameCode, int turn, Map<String, Long> turnResult) {
        log.info("=====================================");
        log.info("SUCCESS USE ABILITY, SAVE TURN RESULT");
        log.info("GAME_CODE : " + gameCode);
        log.info("TURN : " + turn);
        log.info("TURN_RESULT : " + turnResult);
        log.info("=====================================");
    }

    private void logGameStateInfo(List<RoomUserJob> roomUserJobs, int citizenCount, int mafiaCount) {
        log.info("==================================");
        log.info("roomUserJobs: " + roomUserJobs.toString());
        log.info("citizenCount: " + citizenCount);
        log.info("mafiaCount: " + mafiaCount);
        log.info("==================================");
    }

    private void logAbilityResult(String gameCode) {
        log.info("=====================================");
        log.info("SUCCESS SAVE USER ABILITY LOG");
        log.info("GAME_CODE : " + gameCode);
        log.info("=====================================");
    }
}
