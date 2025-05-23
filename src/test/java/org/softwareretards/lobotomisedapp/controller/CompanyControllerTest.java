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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void updateCompanyShouldRedirectToCompanyDetailsOnSuccess() {
        CompanyDto companyDto = new CompanyDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("acme");
        companyDto.setUserDto(userDto);

        when(companyService.updateCompany("acme", companyDto)).thenReturn(companyDto);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        String viewName = companyController.updateCompany("acme", companyDto, result);

        assertEquals("redirect:/companies/acme", viewName);
    }

    @Test
    void deleteCompanyShouldRedirectToCompaniesList() {
        String viewName = companyController.deleteCompany("acme");
        assertEquals("redirect:/companies", viewName);
        verify(companyService).deleteCompany("acme");
    }

    @Test
    void getAssignedTraineeshipsShouldAddTraineeshipsToModelAndReturnListView() {
        List<TraineeshipPositionDto> traineeships = List.of(new TraineeshipPositionDto());
        when(companyService.getAssignedTraineeships("acme")).thenReturn(traineeships);

        Model model = mock(Model.class);
        String viewName = companyController.getAssignedTraineeships("acme", model);

        assertEquals("traineeship/list", viewName);
        verify(model).addAttribute("traineeships", traineeships);
    }

    @Test
    void showTraineeshipFormShouldAddTraineeshipDtoAndCompanyUsernameToModel() {
        Model model = mock(Model.class);
        String viewName = companyController.showTraineeshipForm("acme", model);

        assertEquals("traineeship/form", viewName);
        verify(model).addAttribute(eq("traineeship"), any(TraineeshipPositionDto.class));
        verify(model).addAttribute("companyUsername", "acme");
    }

    @Test
    void announceTraineeshipShouldReturnFormViewWhenValidationFails() {
        TraineeshipPositionDto traineeshipDto = new TraineeshipPositionDto();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String viewName = companyController.announceTraineeship("acme", traineeshipDto, result);

        assertEquals("traineeship/form", viewName);
    }

    @Test
    void announceTraineeshipShouldRedirectToTraineeshipsOnSuccess() {
        TraineeshipPositionDto traineeshipDto = new TraineeshipPositionDto();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        String viewName = companyController.announceTraineeship("acme", traineeshipDto, result);

        assertEquals("redirect:/companies/acme/traineeships", viewName);
        verify(companyService).announceTraineeship(traineeshipDto);
    }

    @Test
    void deleteTraineeshipShouldRedirectToTraineeshipsList() {
        String viewName = companyController.deleteTraineeship(42L);

        assertEquals("redirect:/companies/42/traineeships", viewName);
        verify(companyService).deleteTraineeship(42L);
    }

}
