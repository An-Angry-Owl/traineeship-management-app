package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;

import java.util.List;

public interface TraineeshipRecommendationsStrategy {
    List<TraineeshipPositionDto> recommendTraineeships(Long studentId);
}