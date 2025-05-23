package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.strategy.recommendation.AbstractRecommendationsStrategy;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationsStrategyFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationsServiceImplTest {

    @Mock
    private RecommendationsStrategyFactory strategyFactory;

    @Mock
    private AbstractRecommendationsStrategy strategy;

    @InjectMocks
    private RecommendationsServiceImpl recommendationsService;

    @Test
    void recommendShouldReturnTraineeshipsForValidStudentAndStrategy() {
        Long studentId = 1L;
        RecommendationType type = RecommendationType.COMBINED;
        List<TraineeshipPositionDto> traineeships = List.of(new TraineeshipPositionDto());

        when(strategyFactory.getStrategy(type)).thenReturn(strategy);
        when(strategy.recommendTraineeships(studentId)).thenReturn(traineeships);

        List<TraineeshipPositionDto> result = recommendationsService.recommend(studentId, type);

        assertEquals(traineeships, result);
    }

    @Test
    void recommendShouldReturnEmptyListWhenNoTraineeshipsFound() {
        Long studentId = 1L;
        RecommendationType type = RecommendationType.COMBINED;

        when(strategyFactory.getStrategy(type)).thenReturn(strategy);
        when(strategy.recommendTraineeships(studentId)).thenReturn(List.of());

        List<TraineeshipPositionDto> result = recommendationsService.recommend(studentId, type);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendShouldThrowExceptionForInvalidStrategyType() {
        Long studentId = 1L;
        RecommendationType type = RecommendationType.LOCATION_BASED;

        when(strategyFactory.getStrategy(type)).thenThrow(new IllegalArgumentException("Invalid strategy type"));

        assertThrows(IllegalArgumentException.class, () -> recommendationsService.recommend(studentId, type));
    }
}