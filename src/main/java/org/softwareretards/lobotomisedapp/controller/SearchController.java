package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/{professorId}")
    public String getRecommendations(@PathVariable Long professorId,
                                     @RequestParam("strategy") SearchType strategy,
                                     Model model) {
        List<TraineeshipPositionDto> search = searchService.recommend(professorId, strategy);
        model.addAttribute("recommendations", search);
        model.addAttribute("professorId", professorId);
        model.addAttribute("selectedStrategy", strategy);
        return "search/list"; // Your Thymeleaf or JSP page
    }
}
