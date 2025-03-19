package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        return "students/profile-form"; // thymeleaf template: src/main/resources/templates/students/profile-form.html
    }

    @PostMapping("/students/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("student") StudentDto studentDto,
            Model model
    ) {
        StudentDto savedProfile = studentService.saveProfile(studentDto);
        model.addAttribute("student", savedProfile);
        return "redirect:/students/" + username + "/profile";
    }

    // US5: Apply for Traineeship
    @GetMapping("/students/{username}/apply")
    public String showApplicationForm(@PathVariable String username, Model model) {
        model.addAttribute("application", new TraineeshipApplicationDto());
        return "students/application-form"; // thymeleaf template: students/application-form.html
    }

    @PostMapping("/students/{username}/apply")
    public String submitApplication(
            @PathVariable String username,
            @ModelAttribute("application") TraineeshipApplicationDto applicationDto,
            Model model
    ) {
        TraineeshipApplicationDto createdApplication =
                studentService.applyForTraineeship(username, applicationDto.getPosition().getId());
        model.addAttribute("application", createdApplication);
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
}