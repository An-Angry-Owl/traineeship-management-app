package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    // US13: Profile Management
    @GetMapping("/professors/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        ProfessorDto professorDto = professorService.getProfile(username);
        model.addAttribute("professor", professorDto);
        return "professors/profile-form";
    }

    @PostMapping("/professors/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("professor") ProfessorDto professorDto,
            Model model
    ) {
        ProfessorDto savedProfile = professorService.saveProfile(professorDto);
        model.addAttribute("professor", savedProfile);
        return "redirect:/professors/" + username + "/profile";
    }

    @GetMapping("/professors/{username}/dashboard")
    public String showDashboard(@PathVariable String username, Model model) {
        ProfessorDto professor = professorService.getProfile(username);
        List<TraineeshipPositionDto> supervisedPositions = professorService.getSupervisedPositions(username);

        model.addAttribute("professor", professor);
        model.addAttribute("supervisedPositions", supervisedPositions != null ? supervisedPositions : Collections.emptyList());

        return "professors/dashboard";
    }

    // US14: Supervised Positions
    @GetMapping("/professors/{username}/positions")
    public String listSupervisedPositions(@PathVariable String username, Model model) {
        List<TraineeshipPositionDto> positions = professorService.getSupervisedPositions(username);
        model.addAttribute("positions", positions);
        return "professors/position-list";
    }

    // US15: Evaluation Submission
    @GetMapping("/professors/{username}/positions/{positionId}/evaluate")
    public String showEvaluationForm(
            @PathVariable String username,
            @PathVariable Long positionId,
            Model model
    ) {
        model.addAttribute("evaluation", new EvaluationDto());
        model.addAttribute("positionId", positionId);
        return "professors/evaluation-form";
    }

    @PostMapping("/professors/{username}/positions/{positionId}/evaluate")
    public String submitEvaluation(
            @PathVariable String username,
            @PathVariable Long positionId,
            @ModelAttribute("evaluation") EvaluationDto evaluationDto,
            Model model
    ) {
        EvaluationDto savedEvaluation = professorService.saveEvaluation(username, positionId, evaluationDto);
        model.addAttribute("evaluation", savedEvaluation);
        return "professors/evaluation-confirmation";
    }
}