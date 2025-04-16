package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recommendations")
public class RecommendationsController {

    private final RecommendationsService recommendationsService;

    @Autowired
    public RecommendationsController(RecommendationsService recommendationsService) {
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/{studentId}")
    public String getRecommendations(@PathVariable Long studentId,
                                     @RequestParam("strategy") RecommendationType strategy,
                                     Model model) {
        List<TraineeshipPositionDto> recommendations = recommendationsService.recommend(studentId, strategy);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("studentId", studentId);
        model.addAttribute("selectedStrategy", strategy);
        return "recommendations/list"; // Your Thymeleaf or JSP page
    }
}