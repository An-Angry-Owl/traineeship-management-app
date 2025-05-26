package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.service.ProfessorService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @Mock
    private UserService userService;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private EvaluationMapper evaluationMapper;

    @InjectMocks
    private ProfessorController professorController;

    private ProfessorDto professorDto;
    private User user;
    private TraineeshipPositionDto positionDto;
    private EvaluationDto evaluationDto;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("prof1");
        userDto.setPassword("password");

        professorDto = new ProfessorDto();
        professorDto.setUserDto(userDto);

        user = new User();
        user.setId(1L);
        user.setUsername("prof1");

        positionDto = new TraineeshipPositionDto();
        positionDto.setId(1L);

        evaluationDto = new EvaluationDto();
        evaluationDto.setId(1L);
    }

    @Test
    void showProfileForm_ShouldReturnProfileFormView() {
        when(professorService.getProfile("prof1")).thenReturn(professorDto);

        String viewName = professorController.showProfileForm("prof1", model);

        assertEquals("professors/profile-form", viewName);
        verify(model).addAttribute("professor", professorDto);
    }

    @Test
    void saveProfile_ShouldRedirectToProfile() {
        when(professorService.getProfile("prof1")).thenReturn(professorDto);
        when(professorService.saveProfile(any())).thenReturn(professorDto);

        String viewName = professorController.saveProfile("prof1", professorDto, model);

        assertEquals("redirect:/professors/prof1/profile", viewName);
        verify(model).addAttribute("professor", professorDto);
    }

    @Test
    void listSupervisedPositions_ShouldReturnPositionListView() {
        List<TraineeshipPositionDto> positions = Collections.singletonList(positionDto);
        when(professorService.getSupervisedPositions("prof1")).thenReturn(positions);

        String viewName = professorController.listSupervisedPositions("prof1", model);

        assertEquals("professors/position-list", viewName);
        verify(model).addAttribute("positions", positions);
    }

    @Test
    void submitEvaluation_Success_ShouldRedirectWithSuccessMessage() {
        when(professorService.saveEvaluation("prof1", 1L, evaluationDto))
                .thenReturn(evaluationDto);

        String viewName = professorController.submitEvaluation(
                "prof1", 1L, evaluationDto, redirectAttributes);

        assertEquals("redirect:/professors/prof1/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("success", "Evaluation submitted successfully!");
    }

    @Test
    void showEvaluationForm_ShouldReturnDashboardViewWithExistingEvaluation() {
        EvaluationDto evaluationDto = new EvaluationDto();
        when(professorService.getOrCreateEvaluation(1L)).thenReturn(evaluationDto);

        String viewName = professorController.showEvaluationForm("prof1", 1L, model);

        assertEquals("professors/dashboard", viewName);
        verify(model).addAttribute("evaluation", evaluationDto);
        verify(model).addAttribute("positionId", 1L);
    }

    @Test
    void showEvaluationForm_ShouldReturnDashboardViewWithNullEvaluation() {
        when(professorService.getOrCreateEvaluation(1L)).thenReturn(null);

        String viewName = professorController.showEvaluationForm("prof1", 1L, model);

        assertEquals("professors/dashboard", viewName);
        verify(model).addAttribute("evaluation", null);
        verify(model).addAttribute("positionId", 1L);
    }

    @Test
    void saveProfile_ShouldPreserveUserDtoAndRedirect() {
        ProfessorDto inputDto = new ProfessorDto();
        ProfessorDto existingDto = new ProfessorDto();
        UserDto userDto = new UserDto();
        existingDto.setUserDto(userDto);
        ProfessorDto savedDto = new ProfessorDto();
        savedDto.setUserDto(userDto);

        when(professorService.getProfile("prof1")).thenReturn(existingDto);
        when(professorService.saveProfile(any())).thenReturn(savedDto);

        String viewName = professorController.saveProfile("prof1", inputDto, model);

        assertEquals("redirect:/professors/prof1/profile", viewName);
        verify(model).addAttribute("professor", savedDto);
    }

    @Test
    void submitEvaluation_ShouldRedirectWithErrorMessageOnException() {
        doThrow(new RuntimeException("fail")).when(professorService).saveEvaluation(eq("prof1"), eq(1L), any());

        String viewName = professorController.submitEvaluation("prof1", 1L, new EvaluationDto(), redirectAttributes);

        assertEquals("redirect:/professors/prof1/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "fail");
    }

    @Test
    void submitEvaluation_Failure_ShouldRedirectWithErrorMessage() {
        RuntimeException exception = new RuntimeException("Evaluation error");
        when(professorService.saveEvaluation("prof1", 1L, evaluationDto))
                .thenThrow(exception);

        String viewName = professorController.submitEvaluation(
                "prof1", 1L, evaluationDto, redirectAttributes);

        assertEquals("redirect:/professors/prof1/dashboard", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Evaluation error");
    }

    @Test
    void showDashboard_Authorized_ShouldReturnDashboardView() {
        when(professorService.getProfile("prof1")).thenReturn(professorDto);
        when(professorService.getSupervisedPositions("prof1"))
                .thenReturn(Collections.singletonList(positionDto));

        String viewName = professorController.showDashboard("prof1", user, model);

        assertEquals("professors/dashboard", viewName);
        verify(model).addAttribute("professor", professorDto);
        verify(model).addAttribute("positions", Collections.singletonList(positionDto));
        verify(model).addAttribute("position", Collections.singletonList(positionDto));
        verify(model).addAttribute(eq("evaluation"), any(EvaluationDto.class));
    }

    @Test
    void showDashboard_Unauthorized_ShouldRedirectToAccessDenied() {
        User differentUser = new User();
        differentUser.setUsername("differentUser");

        String viewName = professorController.showDashboard("prof1", differentUser, model);

        assertEquals("redirect:/access-denied", viewName);
    }

    @Test
    void changePassword_Success_ShouldRedirectWithSuccessMessage() {
        String viewName = professorController.changePassword(
                "prof1", "oldPass", "newPass", "newPass", redirectAttributes);

        assertEquals("redirect:/professors/prof1/profile", viewName);
        verify(userService).changePassword("prof1", "oldPass", "newPass", "newPass");
        verify(redirectAttributes).addFlashAttribute("success", "Password changed successfully!");
    }

    @Test
    void changePassword_Failure_ShouldRedirectWithErrorMessage() {
        RuntimeException exception = new RuntimeException("Password error");
        doThrow(exception).when(userService).changePassword("prof1", "oldPass", "newPass", "newPass");

        String viewName = professorController.changePassword(
                "prof1", "oldPass", "newPass", "newPass", redirectAttributes);

        assertEquals("redirect:/professors/prof1/profile", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Password error");
    }
}