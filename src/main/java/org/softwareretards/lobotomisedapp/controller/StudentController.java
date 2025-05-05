package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // US4: Profile Management
    @GetMapping("/students/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        StudentDto studentDto = studentService.retrieveProfile(username);
        model.addAttribute("student", studentDto);
        return "students/profile-form";
    }

    @PostMapping("/students/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("student") StudentDto studentDto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            StudentDto savedProfile = studentService.saveProfile(username, studentDto);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/students/" + username + "/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/" + username + "/profile";
        }
    }

    // US5: Apply for Traineeship
    @GetMapping("/students/{username}/apply")
    public String showApplicationForm(@PathVariable String username, Model model) {
        model.addAttribute("application", new TraineeshipApplicationDto());
        return "students/application-form";
    }

    @PostMapping("/students/{username}/apply")
    public String submitApplication(
            @PathVariable String username,
            @ModelAttribute("application") TraineeshipApplicationDto applicationDto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            TraineeshipApplicationDto createdApplication =
                    studentService.applyForTraineeship(username, applicationDto.getPosition().getId());
            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
            return "redirect:/students/" + username + "/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/" + username + "/apply";
        }
    }

    // US6: Logbook Management
    @GetMapping("/students/{username}/logbook")
    public String showLogbookForm(@PathVariable String username, Model model) {
        model.addAttribute("logbookEntry", new LogbookEntryDto());
        return "students/logbook-form";
    }

    @PostMapping("/students/{username}/logbook")
    public String saveLogbookEntry(
            @PathVariable String username,
            @ModelAttribute("logbookEntry") LogbookEntryDto logbookEntryDto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            studentService.saveLogbookEntry(
                    username,
                    logbookEntryDto.getPosition().getId(),
                    logbookEntryDto.getContent()
            );
            redirectAttributes.addFlashAttribute("success", "Logbook entry saved successfully!");
            return "redirect:/students/" + username + "/logbook";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/" + username + "/logbook";
        }
    }

    @GetMapping("/students/{username}/dashboard")
    public String showDashboard(@PathVariable String username, Model model) {
        StudentDto studentDto = studentService.retrieveProfile(username);
        model.addAttribute("student", studentDto);
        return "students/dashboard";
    }

}