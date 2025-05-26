package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.service.CompanyService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Test
    void companyDashboard_ShouldRedirectToAccessDeniedIfUserIsNotOwner() {
        User user = new User();
        user.setUsername("otherCompany");
        Model model = mock(Model.class);

        String viewName = companyController.companyDashboard("acme", user, model);

        assertEquals("redirect:/access-denied", viewName);
    }

    @Test
    void companyDashboard_ShouldThrowExceptionIfCompanyNotFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("acme");
        Model model = mock(Model.class);

        when(companyService.findCompanyById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> companyController.companyDashboard("acme", user, model));
    }

    @Test
    void companyDashboard_ShouldAddCompanyAndTraineeshipsToModel() {
        User user = new User();
        user.setId(1L);
        user.setUsername("acme");
        Model model = mock(Model.class);
        CompanyDto companyDto = new CompanyDto();
        List<TraineeshipPositionDto> positions = List.of(new TraineeshipPositionDto());

        when(companyService.findCompanyById(1L)).thenReturn(Optional.of(companyDto));
        when(companyService.getTraineeshipPositions("acme")).thenReturn(positions);

        String viewName = companyController.companyDashboard("acme", user, model);

        assertEquals("companies/dashboard", viewName);
        verify(model).addAttribute("company", companyDto);
        verify(model).addAttribute("traineeships", positions);
    }

    @Test
    void showProfileForm_ShouldThrowExceptionIfCompanyNotFound() {
        when(companyService.findCompanyByUsername("acme")).thenReturn(Optional.empty());
        Model model = mock(Model.class);

        assertThrows(NoSuchElementException.class, () -> companyController.showProfileForm("acme", model));
    }

    @Test
    void updateProfile_ShouldCallUpdateAndRedirect() {
        CompanyDto companyDto = new CompanyDto();
        String viewName = companyController.updateProfile("acme", companyDto);

        assertEquals("redirect:/companies/acme/profile", viewName);
        verify(companyService).updateCompany("acme", companyDto);
    }

    @Test
    void showTraineeshipSettings_ShouldRedirectToAccessDeniedIfUserIsNotOwner() {
        User user = new User();
        user.setUsername("otherCompany");
        Model model = mock(Model.class);

        String viewName = companyController.showTraineeshipSettings("acme", 1L, user, model);

        assertEquals("redirect:/access-denied", viewName);
    }

    @Test
    void showTraineeshipSettings_ShouldRedirectToErrorIfPositionNotFound() {
        User user = new User();
        user.setUsername("acme");
        Model model = mock(Model.class);

        when(companyService.getTraineeshipPositionByIdAndCompany(1L, "acme")).thenReturn(null);

        String viewName = companyController.showTraineeshipSettings("acme", 1L, user, model);

        assertEquals("redirect:/error", viewName);
    }

    @Test
    void showTraineeshipSettings_ShouldAddPositionAndCompanyUsernameToModel() {
        User user = new User();
        user.setUsername("acme");
        Model model = mock(Model.class);
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();

        when(companyService.getTraineeshipPositionByIdAndCompany(1L, "acme")).thenReturn(positionDto);

        String viewName = companyController.showTraineeshipSettings("acme", 1L, user, model);

        assertEquals("companies/position-settings", viewName);
        verify(model).addAttribute("position", positionDto);
        verify(model).addAttribute("companyUsername", "acme");
    }

    @Test
    void updateTraineeshipPosition_ShouldRedirectWithErrorIfBindingResultHasErrors() {
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = companyController.updateTraineeshipPosition("acme", 1L, positionDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/companies/acme/traineeships/1/settings", viewName);
    }

    @Test
    void updateTraineeshipPosition_ShouldRedirectWithSuccessOnUpdate() {
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(companyService.updateTraineeshipPosition(positionDto, "acme")).thenReturn(positionDto);

        String viewName = companyController.updateTraineeshipPosition("acme", 1L, positionDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/companies/acme/traineeships/1/settings", viewName);
        verify(redirectAttributes).addFlashAttribute("success", "Position updated successfully!");
    }

    @Test
    void updateTraineeshipPosition_ShouldRedirectWithErrorOnException() {
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(companyService.updateTraineeshipPosition(positionDto, "acme")).thenThrow(new RuntimeException("Update failed"));

        String viewName = companyController.updateTraineeshipPosition("acme", 1L, positionDto, bindingResult, redirectAttributes);

        assertEquals("redirect:/companies/acme/traineeships/1/settings", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Update failed");
    }

    @Test
    void showTraineeshipSettingsForm_ShouldRedirectToAccessDeniedIfUserIsNotOwner() {
        User user = new User();
        user.setUsername("otherCompany");
        Model model = mock(Model.class);

        String viewName = companyController.showTraineeshipSettingsForm("acme", 1L, user, model);

        assertEquals("redirect:/access-denied", viewName);
    }

    @Test
    void showTraineeshipSettingsForm_ShouldAddAllFieldsToModel() {
        User user = new User();
        user.setUsername("acme");
        Model model = mock(Model.class);
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setDescription("desc");
        positionDto.setStartDate(LocalDate.parse("2024-01-01"));
        positionDto.setEndDate(LocalDate.parse("2024-12-31"));
        positionDto.setRequiredSkills("Java");
        positionDto.setTopics("AI");

        when(companyService.getTraineeshipPositionByIdAndCompany(1L, "acme")).thenReturn(positionDto);

        String viewName = companyController.showTraineeshipSettingsForm("acme", 1L, user, model);

        assertEquals("companies/position-settings", viewName);
        verify(model).addAttribute("description", "desc");
        verify(model).addAttribute("startDate", LocalDate.parse("2024-01-01"));
        verify(model).addAttribute("endDate", LocalDate.parse("2024-12-31"));
        verify(model).addAttribute("requiredSkills", "Java");
        verify(model).addAttribute("topics", "AI");
        verify(model).addAttribute("position", positionDto);
        verify(model).addAttribute("companyUsername", "acme");
    }

    @Test
    void deleteTraineeshipPosition_ShouldRedirectWithSuccessOnDelete() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String viewName = companyController.deleteTraineeshipPosition("acme", 1L, redirectAttributes);

        assertEquals("redirect:/companies/acme/dashboard", viewName);
        verify(companyService).deleteTraineeshipPosition(1L, "acme");
        verify(redirectAttributes).addFlashAttribute("success", "Position deleted successfully!");
    }

    @Test
    void deleteTraineeshipPosition_ShouldRedirectWithErrorOnException() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("Delete failed")).when(companyService).deleteTraineeshipPosition(1L, "acme");

        String viewName = companyController.deleteTraineeshipPosition("acme", 1L, redirectAttributes);

        assertEquals("redirect:/companies/acme/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Delete failed");
    }

}
