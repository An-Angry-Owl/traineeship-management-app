package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        return "professors/profile-form"; // src/main/resources/templates/professors/profile-form.html
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

    // US14: Supervised Positions
    @GetMapping("/professors/{username}/positions")
    public String listSupervisedPositions(@PathVariable String username, Model model) {
        List<TraineeshipPositionDto> positions = professorService.getSupervisedPositions(username);
        model.addAttribute("positions", positions);
        return "professors/position-list"; // src/main/resources/templates/professors/position-list.html
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
        return "professors/evaluation-form"; // src/main/resources/templates/professors/evaluation-form.html
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

    @GetMapping("/professors/{username}/dashboard")
    public String showDashboard(
            @PathVariable String username,
            @AuthenticationPrincipal User user,
            Model model) {

        // Authorization check
        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        ProfessorDto professorDto = professorService.getProfile(username);
        List<TraineeshipPositionDto> positions = professorService.getSupervisedPositions(username);

        model.addAttribute("professor", professorDto);
        model.addAttribute("positions", positions);

        return "professors/dashboard";
    }
}