package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/committees")
public class CommitteeController {

    private final CommitteeService committeeService;
    private final UserService userService;
    private final RecommendationsService recommendationsService;
    private final SearchService searchService;

    @Autowired
    public CommitteeController(CommitteeService committeeService,
                               UserService userService,
                               RecommendationsService recommendationsService,
                               SearchService searchService) {
        this.committeeService = committeeService;
        this.userService = userService;
        this.recommendationsService = recommendationsService;
        this.searchService = searchService;
    }


    @PostMapping("/create")
    public String createCommittee(@ModelAttribute CommitteeDto committeeDto) {
        committeeService.createCommittee(committeeDto);
        return "redirect:/committee/list";
    }


    @PostMapping("/edit/{id}")
    public String updateCommittee(@PathVariable Long id,
                                  @ModelAttribute CommitteeDto committeeDto) {
        committeeService.updateCommittee(id, committeeDto);
        return "redirect:/committee/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommittee(@PathVariable Long id) {
        committeeService.deleteCommittee(id);
        return "redirect:/committee/list";
    }

    @PostMapping("/assignProfessor")
    public String assignSupervisingProfessor(
            @RequestParam Long committeeId,
            @RequestParam Long traineeshipId,
            @RequestParam Long professorId,
            RedirectAttributes redirectAttributes) {
        try {
            committeeService.assignSupervisingProfessor(committeeId, traineeshipId, professorId);
            redirectAttributes.addFlashAttribute("success", "Professor assigned successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/committees/professor-list?professorId=" + professorId;
    }


    @GetMapping("/{username}/dashboard")
    public String showDashboard(
            @PathVariable String username,
            @AuthenticationPrincipal User user,
            Model model) {

        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        CommitteeDto committeeDto = committeeService.getCommitteeById(user.getId());
        List<TraineeshipPositionDto> positions = committeeService.getAllTraineeshipPositions();

        model.addAttribute("committee", committeeDto);
        model.addAttribute("positions", positions);
        return "committees/dashboard";
    }

    @GetMapping("/professor-list")
    public String showProfessorList(
            @RequestParam(value = "positionId", required = false) Long positionId,
            @RequestParam(value = "strategy", defaultValue = "NONE") SearchType strategy,
            Model model,
            Authentication authentication) {

        String username = authentication.getName();
        CommitteeDto committeeDto = committeeService.getCommitteeByUsername(username);
        model.addAttribute("committee", committeeDto);

        List<TraineeshipPositionDto> positions = committeeService.getAssignedTraineeshipPositions();
        model.addAttribute("positions", positions);

        if (positionId != null) {
            TraineeshipPositionDto selectedPosition = committeeService.getTraineeshipPositionById(positionId);
            List<ProfessorDto> recommendations = searchService.recommend(positionId, strategy);
            Map<Long, Integer> professorWorkloads = committeeService.getProfessorWorkloads(recommendations);

            model.addAttribute("selectedPosition", selectedPosition);
            model.addAttribute("recommendations", recommendations);
            model.addAttribute("professorWorkloads", professorWorkloads);
            model.addAttribute("selectedStrategy", strategy);
        }

        return "committees/professor-list";
    }

    @GetMapping("/student-list")
    public String showStudentList(
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "strategy", defaultValue = "NONE") RecommendationType strategy,
            Model model,
            Authentication authentication) {

        String username = authentication.getName();
        CommitteeDto committeeDto = committeeService.getCommitteeByUsername(username);
        model.addAttribute("committee", committeeDto);

        List<StudentDto> students = committeeService.getStudentsWithApplications();
        model.addAttribute("students", students);

        if (studentId != null) {
            StudentDto selectedStudent = committeeService.getStudentById(studentId);
            List<TraineeshipPositionDto> recommendations = recommendationsService.recommend(studentId, strategy);

            model.addAttribute("selectedStudent", selectedStudent);
            model.addAttribute("recommendations", recommendations);
            model.addAttribute("selectedStrategy", strategy);
        }

        return "committees/student-list";
    }

    @GetMapping("/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        CommitteeDto committeeDto = committeeService.getCommitteeByUsername(username);
        model.addAttribute("committee", committeeDto);
        return "committees/profile-form";
    }

    @PostMapping("/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("committee") CommitteeDto committeeDto,
            Model model) {

        CommitteeDto savedProfile = committeeService.updateCommitteeProfile(username, committeeDto);
        model.addAttribute("committee", savedProfile);
        return "redirect:/committees/" + username + "/profile";
    }

    @PostMapping("/{username}/change-password")
    public String changePassword(
            @PathVariable String username,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(username, currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/committees/" + username + "/profile";
    }

    @PostMapping("/assign-traineeship")
    public String assignTraineeship(@RequestParam Long committeeId,
                                    @RequestParam Long studentId,
                                    @RequestParam Long traineeshipId,
                                    RedirectAttributes redirectAttributes) {
        try {
            committeeService.assignTraineeshipToStudent(committeeId, studentId, traineeshipId);
            committeeService.acceptApplication(studentId);
            redirectAttributes.addFlashAttribute("success", "Traineeship assigned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign traineeship: " + e.getMessage());
        }
        return "redirect:/committees/student-list?studentId=" + studentId;
    }

    @GetMapping("/{username}/traineeships/{positionId}/info")
    public String showEvaluationInfo(
            @PathVariable String username,
            @PathVariable Long positionId,
            @AuthenticationPrincipal User user,
            Model model) {

        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        CommitteeDto committeeDto = committeeService.getCommitteeById(user.getId());
        List<TraineeshipPositionDto> positions = committeeService.getAllTraineeshipPositions();
        EvaluationDto evaluation = committeeService.getEvaluationByPositionId(positionId);

        model.addAttribute("committee", committeeDto);
        model.addAttribute("positions", positions);
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("position", committeeService.getTraineeshipPositionById(positionId));

        return "committees/dashboard";
    }

    @PostMapping("/{username}/update-final-mark")
    public String updateFinalMark(@PathVariable String username,
                                  @RequestParam Long positionId,
                                  @RequestParam FinalMark finalMark,
                                  RedirectAttributes redirectAttributes) {
        try {
            committeeService.updateFinalMark(positionId, finalMark);
            redirectAttributes.addFlashAttribute("success", "Final mark updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating final mark: " + e.getMessage());
        }
        return "redirect:/committees/" + username + "/dashboard";
    }

    @GetMapping("/committees/evaluations/{positionId}")
    @ResponseBody
    public EvaluationDto getEvaluationData(@PathVariable Long positionId) {
        return committeeService.getEvaluationByPositionId(positionId);
    }
}
