package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;

import java.util.List;

public abstract class AbstractRecommendationsStrategy implements TraineeshipRecommendationsStrategy {
    public abstract RecommendationType getType();

    @Override
    public abstract List<TraineeshipPositionDto> recommendTraineeships(Long studentId);
}