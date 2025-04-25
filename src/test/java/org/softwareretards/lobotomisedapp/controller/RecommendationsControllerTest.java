package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationsControllerTest {

    @Mock
    private RecommendationsService recommendationsService;

    @InjectMocks
    private RecommendationsController recommendationsController;

    @Test
    void getRecommendationsShouldReturnListViewWithRecommendations() {
        Long studentId = 1L;
        RecommendationType strategy = RecommendationType.COMBINED;
        List<TraineeshipPositionDto> recommendations = List.of(new TraineeshipPositionDto());

        when(recommendationsService.recommend(studentId, strategy)).thenReturn(recommendations);

        Model model = mock(Model.class);
        String viewName = recommendationsController.getRecommendations(studentId, strategy, model);

        assertEquals("recommendations/list", viewName);
        verify(model).addAttribute("recommendations", recommendations);
        verify(model).addAttribute("studentId", studentId);
        verify(model).addAttribute("selectedStrategy", strategy);
    }

    @Test
    void getRecommendationsShouldHandleEmptyRecommendationsList() {
        Long studentId = 1L;
        RecommendationType strategy = RecommendationType.COMBINED;

        when(recommendationsService.recommend(studentId, strategy)).thenReturn(List.of());

        Model model = mock(Model.class);
        String viewName = recommendationsController.getRecommendations(studentId, strategy, model);

        assertEquals("recommendations/list", viewName);
        verify(model).addAttribute("recommendations", List.of());
        verify(model).addAttribute("studentId", studentId);
        verify(model).addAttribute("selectedStrategy", strategy);
    }
}
