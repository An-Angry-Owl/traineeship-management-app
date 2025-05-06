package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.user.CompanyMapper;
import org.softwareretards.lobotomisedapp.repository.user.CompanyRepository;
import org.softwareretards.lobotomisedapp.service.CompanyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    // Display all companies
    @GetMapping
    public String listCompanies(Model model) {
        List<CompanyDto> companies = companyService.getAllCompanies();
        model.addAttribute("company", companies);
        return "companies/list";
    }

    // Find company by username
    @GetMapping("/{username}")
    public String getCompanyByUsername(@PathVariable String username, Model model) {
        Optional<CompanyDto> company = companyService.findCompanyByUsername(username);
        model.addAttribute("company", company);
        return "companies/details";
    }

    // Find company by name
    @GetMapping("/searchByName")
    public String findCompanyByName(@RequestParam String name, Model model) {
        Optional<CompanyDto> companies = companyService.findCompanyByName(name);
        model.addAttribute("company", companies);
        return "companies/list";
    }

    // Find company by location
    @GetMapping("/searchByLocation")
    public String findCompanyByLocation(@RequestParam String location, Model model) {
        List<CompanyDto> companies = companyService.findCompanyByLocation(location);
        model.addAttribute("company", companies);
        return "companies/list";
    }

    // Show form to create a new company
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new CompanyDto());
        return "companies/form";
    }

    // Process new company submission
    @PostMapping
    public String createCompany(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "companies/form";
        }
        CompanyDto savedCompany = companyService.createCompany(companyDto);
        return "redirect:/companies/" + savedCompany.getUserDto().getUsername();
    }

    // Show form to edit an existing company
    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        Optional<CompanyDto> company = companyService.findCompanyByUsername(username);
        model.addAttribute("company", company);
        return "companies/form";
    }

    // Process company update
    @PostMapping("/update/{username}")
    public String updateCompany(@PathVariable String username, @Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "companies/form";
        }
        CompanyDto savedCompany = companyService.updateCompany(username, companyDto);
        return "redirect:/companies/" + savedCompany.getUserDto().getUsername();
    }

    // Delete a company
    @GetMapping("/delete/{username}")
    public String deleteCompany(@PathVariable String username) {
        companyService.deleteCompany(username);
        return "redirect:/companies";
    }

    // Get traineeship positions announced by a company
    @GetMapping("/{username}/traineeships")
    public String getTraineeshipPositions(@PathVariable String username, Model model) {
        List<TraineeshipPositionDto> traineeships = companyService.getTraineeshipPositions(username);
        model.addAttribute("traineeships", traineeships);
        return "traineeship/list";
    }

    // Get assigned traineeships
    @GetMapping("/{username}/traineeships/assigned")
    public String getAssignedTraineeships(@PathVariable String username, Model model) {
        List<TraineeshipPositionDto> traineeships = companyService.getAssignedTraineeships(username);
        model.addAttribute("traineeships", traineeships);
        return "traineeship/list";
    }

    // Show form to announce a new traineeship
    @GetMapping("/{username}/traineeships/new")
    public String showTraineeshipForm(@PathVariable String username, Model model) {
        TraineeshipPositionDto traineeshipDto = new TraineeshipPositionDto();
        model.addAttribute("traineeship", traineeshipDto);
        model.addAttribute("companyUsername", username);
        return "traineeship/form";
    }

    // Announce a new traineeship
    @PostMapping("/{username}/traineeships")
    public String announceTraineeship(@PathVariable String username,
                                      @Valid @ModelAttribute("traineeship") TraineeshipPositionDto traineeshipDto,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "traineeship/form";
        }
        companyService.announceTraineeship(traineeshipDto);
        return "redirect:/companies/" + username + "/traineeships";
    }

    // Delete a traineeship
    @GetMapping("/traineeships/delete/{traineeshipId}")
    public String deleteTraineeship(@PathVariable Long traineeshipId) {
        companyService.deleteTraineeship(traineeshipId);
        return "redirect:/companies/" + traineeshipId + "/traineeships";
    }

    // Show evaluation form for a traineeship
    @GetMapping("/traineeships/{traineeshipId}/evaluate")
    public String showEvaluationForm(@PathVariable Long traineeshipId, Model model) {
        model.addAttribute("traineeshipId", traineeshipId);
        return "evaluation/form";
    }

    // Submit traineeship evaluation
    @PostMapping("/traineeships/{traineeshipId}/evaluate")
    public String evaluateTrainee(
            @PathVariable Long traineeshipId,
            @RequestParam Integer motivation,
            @RequestParam Integer effectiveness,
            @RequestParam Integer efficiency) {

        String username = String.valueOf(companyService.evaluateTrainee(traineeshipId, motivation, effectiveness, efficiency));
        return "redirect:/companies/" + username + "/traineeships/" + traineeshipId;
    }

    @GetMapping("/{username}/dashboard")
    public String companyDashboard(
            @PathVariable String username,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        // Verify username matches logged-in user
        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        Optional<CompanyDto> company = companyService.findCompanyById(user.getId());
        List<TraineeshipPositionDto> positions = companyService.getTraineeshipPositions(user.getUsername());

        model.addAttribute("company", company);
        model.addAttribute("traineeships", positions);
        return "companies/dashboard";
    }

    @PostMapping("/{username}/traineeships/new")
    public String handleNewPosition(
            @PathVariable String username,
            @ModelAttribute TraineeshipPositionDto positionDto
    ) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        positionDto.setCompany(CompanyMapper.toDto(company));
        companyService.announceTraineeship(positionDto);
        return "redirect:/companies/" + username + "/dashboard";
    }

    // Show profile form
    @GetMapping("/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        Optional<CompanyDto> company = companyService.findCompanyByUsername(username);
        model.addAttribute("company", company.orElseThrow());
        return "companies/profile-form";
    }

    // Handle profile update
    @PostMapping("/{username}/profile")
    public String updateProfile(@PathVariable String username,
                                @ModelAttribute CompanyDto companyDto) {
        companyService.updateCompany(username, companyDto);
        return "redirect:/companies/" + username + "/profile";
    }
}
