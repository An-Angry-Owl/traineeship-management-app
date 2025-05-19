package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
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

    @GetMapping("/{positionId}")
    public String getSearch(@PathVariable Long positionId,
                            @RequestParam("strategy") SearchType strategy,
                            Model model) {
        List<ProfessorDto> search = searchService.recommend(positionId, strategy);
        model.addAttribute("recommendations", search);
        model.addAttribute("positionId", positionId);
        model.addAttribute("selectedStrategy", strategy);
        return "search/list"; // Thymeleaf template here
    }
}
