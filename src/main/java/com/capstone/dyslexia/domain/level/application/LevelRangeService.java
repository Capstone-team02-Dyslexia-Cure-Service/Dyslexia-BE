package com.capstone.dyslexia.domain.level.application;

import com.capstone.dyslexia.domain.level.config.BasicTestLevelRangeConfig;
import com.capstone.dyslexia.domain.level.config.LevelRangeConfig;
import com.capstone.dyslexia.domain.member.domain.Member;
import com.capstone.dyslexia.domain.question.domain.QuestionResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelRangeService {

    private final LevelRangeConfig levelRangeConfig;

    private final BasicTestLevelRangeConfig basicTestLevelRangeConfig;

    public Optional<LevelRangeConfig.LevelRange> getLevelRange(Double level) {
        List<LevelRangeConfig.LevelRange> levelRanges = levelRangeConfig.getLevelRangeList();
        for (LevelRangeConfig.LevelRange range : levelRanges) {
            if (level >= range.getMin() && level < range.getMax()) {
                return Optional.of(range);
            }
        }
        return Optional.empty();
    }

    public Optional<EnumMap<QuestionResponseType, Double>> getQuestionResponseProbability(Member member) {
        return getLevelRange(member.getLevel()).map(LevelRangeConfig.LevelRange::getProbabilitiesAsEnumMap);
    }

    public Optional<BasicTestLevelRangeConfig> getBasicTestLevelRangeConfig() {
        if (basicTestLevelRangeConfig == null) {
            return Optional.empty();
        }
        return Optional.of(basicTestLevelRangeConfig);
    }

    public Optional<EnumMap<QuestionResponseType, Double>> getBasicTestQuestionResponseProbability() {
        return getBasicTestLevelRangeConfig().map(BasicTestLevelRangeConfig::getProbabilitiesAsEnumMap);
    }

}
