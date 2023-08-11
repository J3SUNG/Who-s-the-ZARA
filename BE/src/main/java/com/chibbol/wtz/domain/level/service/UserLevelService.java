package com.chibbol.wtz.domain.level.service;

import com.chibbol.wtz.domain.job.entity.UserAbilityLog;
import com.chibbol.wtz.domain.level.config.WeightProperties;
import com.chibbol.wtz.domain.level.dto.LevelResultDTO;
import com.chibbol.wtz.domain.level.entity.UserLevel;
import com.chibbol.wtz.domain.level.repository.UserLevelRepository;
import com.sun.nio.sctp.PeerAddressChangeNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserLevelService {
    private final Long EXP_VALUE = 100L;    // 1레벨 경험치
    private final Double EXP_MULTI = 1.1;   // 레벨당 경험치 배수
    private List<LevelResultDTO> levelResults;
    private Map<Long, Double> jobWeightMap;
    private static Long TOTAL_EXP;
    private static Double totalWeight;
    private final WeightMappingService weightMappingService;
    private final UserLevelRepository userLevelRepository;
    private final WeightProperties weightProperties;

    // 승패 / 능력 성공 여부에 따라 경험치 추가
    @Transactional
    public List<LevelResultDTO> updateExp(List<UserAbilityLog> userAbilityLogs){
        // 맵에 유저별 가중치 저장하고 -> 각 가중치를 다 더해서 Total 값이랑 나누면 1점당 exp 나옴 -> 이 exp를 각 가중치랑 곱해서 저장
        Map<Long, Double> userWeight = new HashMap<>();
        totalWeight = 0.0;

        jobWeightMap = weightMappingService.getJobWeightMap(); // jobWeight Map 가져오기
        levelResults = new ArrayList<>(); // 반환할 결과 List
        TOTAL_EXP = EXP_VALUE * userAbilityLogs.size(); // 한 게임에서 나눠가질 수 있는 총 Exp는 100 * 게임 참여 인원

        // 각 가중치 구해서 Map에 저장
        for(UserAbilityLog user: userAbilityLogs) {
            userWeight.put(user.getUser().getUserSeq(),getUserWeight(user, jobWeightMap, userWeight));
        }

        // 각 유저별 가중치랑 곱해서 exp 저장
        Long expValue = (long)(TOTAL_EXP/totalWeight);
        for(Map.Entry<Long, Double> entry: userWeight.entrySet()){
            UserLevel userLevel = userLevelRepository.findByUserUserSeq(entry.getKey());
            LevelResultDTO levelResult = new LevelResultDTO();
            levelUp(userLevel, (long) (entry.getValue()*expValue), levelResult);

            levelResults.add(levelResult);
        }

        return levelResults;
    }

    public void levelUp(UserLevel userLevel, Long exp, LevelResultDTO levelResult){
        int level = userLevel.getLevel();
        boolean levelUpCheck = false; // levelUp 했는지
        Long userExp = userLevel.getExp() + exp; // 원래 exp + 이번 게임에서 얻은 exp
        Long levelUpExp = getLevelUpExp(level); // 해당 레벨의 maxExp

        // 레벨 갱신 : 만약 현재 exp가 level의 exp 보다 크면 레벨 업
        while(userExp >= levelUpExp){
            levelUpCheck = true;
            level = level + 1;
            userExp = userExp - levelUpExp;
            levelUpExp = getLevelUpExp(level);
        }

        levelResult.setUserSeq(userLevel.getUser().getUserSeq());
        levelResult.setLevel(level);
        levelResult.setCurrentExp(userExp);
        levelResult.setMaxExp(getLevelUpExp(level));
        levelResult.setExpValue(exp);
        levelResult.setLevelUp(levelUpCheck);

        userLevelRepository.save(userLevel.update(UserLevel.builder().level(level).exp(exp).build()));
    }

    public Double getUserWeight(UserAbilityLog userAbilityLog, Map<Long, Double> jobWeightMap, Map<Long, Double> userWeight){
        Double gameResultWeight = 0.0;
        Double jobWeight = 0.0;
        Double abilityWeight = 0.0;

        // 직업별 가중치
        jobWeight = jobWeightMap.get(userAbilityLog.getJob().getJobSeq());

        // 게임 결과에 따라 exp 부여
        if(userAbilityLog.isResult()){ // 이겼을 때
            gameResultWeight = weightProperties.getWin();
        }

        // 능력 성공 횟수에 따라 exp 추가 부여
        abilityWeight = userAbilityLog.getAbilitySuccessCount() * weightProperties.getAbility();
        totalWeight += (jobWeight + gameResultWeight + abilityWeight);

        return jobWeight + gameResultWeight + abilityWeight;
    }

    // 레벨에 따른 exp 계산
    public Long getLevelUpExp(int level){
        return (long) (EXP_VALUE * Math.pow(EXP_MULTI, level - 1));
    }
}
