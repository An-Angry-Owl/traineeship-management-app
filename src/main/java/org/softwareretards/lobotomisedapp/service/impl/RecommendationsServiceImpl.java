package org.softwareretards.lobotomisedapp.service.impl;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationsStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

    private final RecommendationsStrategyFactory strategyFactory;

    @Autowired
    public RecommendationsServiceImpl(RecommendationsStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public List<TraineeshipPositionDto> recommend(Long studentId, RecommendationType type) {
        return strategyFactory.getStrategy(type).recommendTraineeships(studentId);
    }
}
