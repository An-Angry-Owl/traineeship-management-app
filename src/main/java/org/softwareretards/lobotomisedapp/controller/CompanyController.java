package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    //Display all companies
    @GetMapping
    public String listCompanies(Model model) {
        List<CompanyDto> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "company/list";
    }

    //Find company by ID
    @GetMapping("/{id}")
    public String getCompanyById(@PathVariable Long id, Model model) {
        CompanyDto company = companyService.findCompanyById(id);
        model.addAttribute("company", company);
        return "company/details";
    }

    //Find company by name
    @GetMapping("/searchByName")
    public String findCompanyByName(@RequestParam String name, Model model) {
        List<CompanyDto> companies = companyService.findCompanyByName(name);
        model.addAttribute("companies", companies);
        return "company/list";
    }

    //Find company by location
    @GetMapping("/searchByLocation")
    public String findCompanyByLocation(@RequestParam String location, Model model) {
        List<CompanyDto> companies = companyService.findCompanyByLocation(location);
        model.addAttribute("companies", companies);
        return "company/list";
    }

    //Show form to create a new company
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new CompanyDto());
        return "company/form"; // Returns company/form.html
    }

    //Process new company submission
    @PostMapping
    public String createCompany(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "company/form";
        }
        CompanyDto savedCompany = companyService.createCompany(companyDto); // Use returned value
        return "redirect:/companies/" + savedCompany.getUserDto().getId();
    }

    //Show form to edit an existing company
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CompanyDto company = companyService.findCompanyById(id);
        model.addAttribute("company", company);
        return "company/form";
    }

    //Process company update
    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable Long id, @Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "company/form";
        }
        CompanyDto savedCompany = companyService.updateCompany(id, companyDto);
        return "redirect:/companies" + savedCompany.getUserDto().getId();
    }

    //Delete a company
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }

    //Get traineeship positions announced by a company
    @GetMapping("/{id}/traineeships")
    public String getTraineeshipPositions(@PathVariable Long id, Model model) {
        List<TraineeshipPositionDto> traineeships = companyService.getTraineeshipPositions(id);
        model.addAttribute("traineeships", traineeships);
        return "traineeship/list"; // Returns traineeship/list.html
    }

    //Get assigned traineeships
    @GetMapping("/{id}/traineeships/assigned")
    public String getAssignedTraineeships(@PathVariable Long id, Model model) {
        List<TraineeshipPositionDto> traineeships = companyService.getAssignedTraineeships(id, true);
        model.addAttribute("traineeships", traineeships);
        return "traineeship/list";
    }

    //Show form to announce a new traineeship
    @GetMapping("/{id}/traineeships/new")
    public String showTraineeshipForm(@PathVariable Long id, Model model) {
        TraineeshipPositionDto traineeshipDto = new TraineeshipPositionDto();
        traineeshipDto.setId(id); // Pre-set company ID
        model.addAttribute("traineeship", traineeshipDto);
        return "traineeship/form"; // Returns traineeship/form.html
    }

    //Announce a new traineeship
    @PostMapping("/{id}/traineeships")
    public String announceTraineeship(@PathVariable Long id, @Valid @ModelAttribute("traineeship") TraineeshipPositionDto traineeshipDto, BindingResult result) {
        if (result.hasErrors()) {
            return "traineeship/form";
        }
        traineeshipDto.setId(id);
        companyService.announceTraineeship(traineeshipDto);
        return "redirect:/companies/" + id + "/traineeships";
    }

    //Delete a traineeship
    @GetMapping("/traineeships/delete/{traineeshipId}")
    public String deleteTraineeship(@PathVariable Long traineeshipId) {
        companyService.deleteTraineeship(traineeshipId);
        return "redirect:/companies";
    }

    //Show evaluation form for a traineeship
    @GetMapping("/traineeships/{traineeshipId}/evaluate")
    public String showEvaluationForm(@PathVariable Long traineeshipId, Model model) {
        model.addAttribute("traineeshipId", traineeshipId);
        return "evaluation/form"; // Returns evaluation/form.html
    }

    //Submit traineeship evaluation
    @PostMapping("/traineeships/{traineeshipId}/evaluate")
    public String evaluateTrainee(
            @PathVariable Long traineeshipId,
            @RequestParam Integer motivation,
            @RequestParam Integer effectiveness,
            @RequestParam Integer efficiency) {

        companyService.evaluateTrainee(traineeshipId, motivation, effectiveness, efficiency);
        return "redirect:/companies/traineeships/" + traineeshipId;
    }
}