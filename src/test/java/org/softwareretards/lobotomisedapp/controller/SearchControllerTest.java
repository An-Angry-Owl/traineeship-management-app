package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchControllerTest {

    private final SearchService searchService = mock(SearchService.class);
    private final SearchController searchController = new SearchController(searchService);

    @Test
    void getSearchShouldAddRecommendationsPositionIdAndStrategyToModel() {
        Model model = mock(Model.class);
        Long positionId = 42L;
        SearchType strategy = SearchType.INTEREST_BASED;
        List<ProfessorDto> recommendations = List.of(new ProfessorDto());
        when(searchService.recommend(positionId, strategy)).thenReturn(recommendations);

        String viewName = searchController.getSearch(positionId, strategy, model);

        verify(model).addAttribute("recommendations", recommendations);
        verify(model).addAttribute("positionId", positionId);
        verify(model).addAttribute("selectedStrategy", strategy);
        assertEquals("search/list", viewName);
    }

    @Test
    void getSearchShouldHandleEmptyRecommendationsList() {
        Model model = mock(Model.class);
        Long positionId = 99L;
        SearchType strategy = SearchType.WORKLOAD;
        when(searchService.recommend(positionId, strategy)).thenReturn(List.of());

        String viewName = searchController.getSearch(positionId, strategy, model);

        verify(model).addAttribute("recommendations", List.of());
        verify(model).addAttribute("positionId", positionId);
        verify(model).addAttribute("selectedStrategy", strategy);
        assertEquals("search/list", viewName);
    }

    @Test
    void getSearchShouldHandleNullRecommendationsFromService() {
        Model model = mock(Model.class);
        Long positionId = 1L;
        SearchType strategy = SearchType.INTEREST_BASED;
        when(searchService.recommend(positionId, strategy)).thenReturn(null);

        String viewName = searchController.getSearch(positionId, strategy, model);

        verify(model).addAttribute("recommendations", (Object) null);
        verify(model).addAttribute("positionId", positionId);
        verify(model).addAttribute("selectedStrategy", strategy);
        assertEquals("search/list", viewName);
    }
}