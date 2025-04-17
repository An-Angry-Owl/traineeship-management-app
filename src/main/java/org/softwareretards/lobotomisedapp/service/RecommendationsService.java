package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;

import java.util.List;

public interface RecommendationsService {
    List<TraineeshipPositionDto> recommend(Long studentId, RecommendationType type);
}
