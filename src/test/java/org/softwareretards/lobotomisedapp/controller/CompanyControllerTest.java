package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.service.CompanyService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void listCompaniesShouldReturnListViewWithAllCompanies() {
        List<CompanyDto> companies = List.of(new CompanyDto());
        when(companyService.getAllCompanies()).thenReturn(companies);

        Model model = mock(Model.class);
        String viewName = companyController.listCompanies(model);

        assertEquals("company/list", viewName);
        verify(model).addAttribute("companies", companies);
    }

    @Test
    void getCompanyByIdShouldReturnDetailsViewWithCompanyDto() {
        Long companyId = 1L;
        CompanyDto companyDto = new CompanyDto();
        when(companyService.findCompanyById(companyId)).thenReturn(companyDto);

        Model model = mock(Model.class);
        String viewName = companyController.getCompanyById(companyId, model);

        assertEquals("company/details", viewName);
        verify(model).addAttribute("company", companyDto);
    }

    @Test
    void findCompanyByNameShouldReturnListViewWithMatchingCompanies() {
        String name = "TechCorp";
        List<CompanyDto> companies = List.of(new CompanyDto());
        when(companyService.findCompanyByName(name)).thenReturn(companies);

        Model model = mock(Model.class);
        String viewName = companyController.findCompanyByName(name, model);

        assertEquals("company/list", viewName);
        verify(model).addAttribute("companies", companies);
    }

    @Test
    void createCompanyShouldRedirectToDetailsViewAfterSaving() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setUserDto(new UserDto());
        companyDto.getUserDto().setId(1L);

        when(companyService.createCompany(companyDto)).thenReturn(companyDto);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        String viewName = companyController.createCompany(companyDto, result);

        assertEquals("redirect:/companies/1", viewName);
    }

    @Test
    void createCompanyShouldReturnFormViewWhenValidationFails() {
        CompanyDto companyDto = new CompanyDto();

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String viewName = companyController.createCompany(companyDto, result);

        assertEquals("company/form", viewName);
    }

    @Test
    void deleteCompanyShouldRedirectToListViewAfterDeletion() {
        Long companyId = 1L;

        String viewName = companyController.deleteCompany(companyId);

        assertEquals("redirect:/companies", viewName);
        verify(companyService).deleteCompany(companyId);
    }

    @Test
    void getTraineeshipPositionsShouldReturnListViewWithTraineeships() {
        Long companyId = 1L;
        List<TraineeshipPositionDto> traineeships = List.of(new TraineeshipPositionDto());
        when(companyService.getTraineeshipPositions(companyId)).thenReturn(traineeships);

        Model model = mock(Model.class);
        String viewName = companyController.getTraineeshipPositions(companyId, model);

        assertEquals("traineeship/list", viewName);
        verify(model).addAttribute("traineeships", traineeships);
    }

    @Test
    void evaluateTraineeShouldRedirectToTraineeshipDetailsAfterEvaluation() {
        Long traineeshipId = 1L;

        String viewName = companyController.evaluateTrainee(traineeshipId, 8, 9, 10);

        assertEquals("redirect:/companies/traineeships/1", viewName);
        verify(companyService).evaluateTrainee(traineeshipId, 8, 9, 10);
    }
}
