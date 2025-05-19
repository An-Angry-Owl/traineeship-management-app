package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    @Autowired
    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    // US4: Profile Management
    @GetMapping("/students/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        StudentDto studentDto = studentService.retrieveProfile(username);
        model.addAttribute("student", studentDto);
        return "students/profile-form"; // thymeleaf template: src/main/resources/templates/students/profile-form.html
    }

    @PostMapping("/students/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("student") StudentDto studentDto,
            Model model
    ) {
        StudentDto savedProfile = studentService.saveProfile(username, studentDto);
        model.addAttribute("student", savedProfile);
        return "redirect:/students/" + username + "/profile";
    }

    // US5: Apply for Traineeship
    @GetMapping("/students/{username}/apply")
    public String showApplicationForm(@PathVariable String username, Model model) {
        model.addAttribute("application", new TraineeshipApplicationDto());
        return "students/application-form"; // thymeleaf template: students/application-form.html
    }

    @PostMapping("/students/{username}/apply/{positionId}")
    public String submitApplication(
            @PathVariable String username,
            @PathVariable Long positionId, // Correctly captures the ID
            Model model
    ) {
        TraineeshipApplicationDto createdApplication =
                studentService.applyForTraineeship(username, positionId);
        return "students/application-confirmation";
    }

    // US6: Logbook Management
    @GetMapping("/students/{username}/logbook")
    public String showLogbookForm(@PathVariable String username, Model model) {
        model.addAttribute("logbookEntry", new LogbookEntryDto());
        return "students/logbook-form"; // thymeleaf template: students/logbook-form.html
    }

    @PostMapping("/students/{username}/logbook")
    public String saveLogbookEntry(
            @PathVariable String username,
            @ModelAttribute("logbookEntry") LogbookEntryDto logbookEntryDto,
            Model model
    ) {
        LogbookEntryDto savedEntry = studentService.saveLogbookEntry(
                username,
                logbookEntryDto.getPosition().getId(),
                logbookEntryDto.getContent()
        );
        model.addAttribute("entry", savedEntry);
        return "students/logbook-confirmation";
    }

    @PostMapping("/students/{username}/change-password")
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
        return "redirect:/students/" + username + "/profile";
    }

    @GetMapping("/students/{username}/dashboard")
    public String showDashboard(
            @PathVariable String username,
            Model model
    ) {
        StudentDto studentDto = studentService.retrieveProfile(username);
        model.addAttribute("student", studentDto);

        // Add this mock data - you'll need to implement proper service later
        TraineeshipPositionDto currentTraineeship = studentService.getCurrentTraineeship(username);
        model.addAttribute("currentTraineeship", currentTraineeship);

        return "students/dashboard";
    }

    @GetMapping("/students/{username}/traineeships")
    public String showAvailableTraineeships(
            @PathVariable String username,
            @AuthenticationPrincipal User user,
            Model model) {

        // Authorization check
        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        List<TraineeshipPositionDto> openPositions = studentService.getOpenTraineeshipPositions();

        // Add to model
        model.addAttribute("positions", openPositions);
        model.addAttribute("student", studentService.retrieveProfile(username));
        model.addAttribute("application", new TraineeshipApplicationDto());

        return "students/traineeship-list";
    }
}