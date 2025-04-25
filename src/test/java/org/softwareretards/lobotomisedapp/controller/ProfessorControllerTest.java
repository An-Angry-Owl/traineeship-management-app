package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.service.ProfessorService;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    @Test
    void showProfileFormShouldReturnProfileFormViewWithProfessorDto() {
        String username = "professor123";
        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setUserDto(new UserDto());
        professorDto.getUserDto().setUsername(username);

        when(professorService.getProfile(username)).thenReturn(professorDto);

        Model model = mock(Model.class);
        String viewName = professorController.showProfileForm(username, model);

        assertEquals("professors/profile-form", viewName);
        verify(model).addAttribute("professor", professorDto);
    }

    @Test
    void saveProfileShouldRedirectToProfileViewAfterSaving() {
        String username = "professor123";
        ProfessorDto professorDto = new ProfessorDto();
        ProfessorDto savedProfile = new ProfessorDto();

        when(professorService.saveProfile(professorDto)).thenReturn(savedProfile);

        Model model = mock(Model.class);
        String viewName = professorController.saveProfile(username, professorDto, model);

        assertEquals("redirect:/professors/" + username + "/profile", viewName);
        verify(model).addAttribute("professor", savedProfile);
    }

    @Test
    void listSupervisedPositionsShouldReturnPositionListViewWithPositions() {
        String username = "professor123";
        List<TraineeshipPositionDto> positions = List.of(new TraineeshipPositionDto());

        when(professorService.getSupervisedPositions(username)).thenReturn(positions);

        Model model = mock(Model.class);
        String viewName = professorController.listSupervisedPositions(username, model);

        assertEquals("professors/position-list", viewName);
        verify(model).addAttribute("positions", positions);
    }

    @Test
    void showEvaluationFormShouldReturnEvaluationFormViewWithNewEvaluationDto() {
        String username = "professor123";
        Long positionId = 1L;

        Model model = mock(Model.class);
        String viewName = professorController.showEvaluationForm(username, positionId, model);

        assertEquals("professors/evaluation-form", viewName);
        verify(model).addAttribute(eq("evaluation"), any(EvaluationDto.class));
        verify(model).addAttribute("positionId", positionId);
    }

    @Test
    void submitEvaluationShouldReturnConfirmationViewAfterSavingEvaluation() {
        String username = "professor123";
        Long positionId = 1L;
        EvaluationDto evaluationDto = new EvaluationDto();
        EvaluationDto savedEvaluation = new EvaluationDto();

        when(professorService.saveEvaluation(username, positionId, evaluationDto)).thenReturn(savedEvaluation);

        Model model = mock(Model.class);
        String viewName = professorController.submitEvaluation(username, positionId, evaluationDto, model);

        assertEquals("professors/evaluation-confirmation", viewName);
        verify(model).addAttribute("evaluation", savedEvaluation);
    }
}
