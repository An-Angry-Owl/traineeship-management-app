package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeControllerTest {

    @Mock
    private CommitteeService committeeService;

    @InjectMocks
    private CommitteeController committeeController;

    @Test
    void getAllCommitteesShouldReturnListViewWithCommittees() {
        List<CommitteeDto> committees = List.of(new CommitteeDto());
        when(committeeService.getAllCommittees()).thenReturn(committees);

        Model model = mock(Model.class);
        String viewName = committeeController.getAllCommittees(model);

        assertEquals("committee/list", viewName);
        verify(model).addAttribute("committees", committees);
    }

    @Test
    void getCommitteeByIdShouldReturnViewWithCommitteeDto() {
        Long committeeId = 1L;
        CommitteeDto committeeDto = new CommitteeDto();
        when(committeeService.getCommitteeById(committeeId)).thenReturn(committeeDto);

        Model model = mock(Model.class);
        String viewName = committeeController.getCommitteeById(committeeId, model);

        assertEquals("committee/view", viewName);
        verify(model).addAttribute("committee", committeeDto);
    }

    @Test
    void createCommitteeShouldRedirectToListAfterSaving() {
        CommitteeDto committeeDto = new CommitteeDto();

        String viewName = committeeController.createCommittee(committeeDto);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).createCommittee(committeeDto);
    }

    @Test
    void showUpdateFormShouldReturnEditViewWithCommitteeDto() {
        Long committeeId = 1L;
        CommitteeDto committeeDto = new CommitteeDto();
        when(committeeService.getCommitteeById(committeeId)).thenReturn(committeeDto);

        Model model = mock(Model.class);
        String viewName = committeeController.showUpdateForm(committeeId, model);

        assertEquals("committee/edit", viewName);
        verify(model).addAttribute("committeeDto", committeeDto);
    }

    @Test
    void updateCommitteeShouldRedirectToListAfterUpdating() {
        Long committeeId = 1L;
        CommitteeDto committeeDto = new CommitteeDto();

        String viewName = committeeController.updateCommittee(committeeId, committeeDto);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).updateCommittee(committeeId, committeeDto);
    }

    @Test
    void deleteCommitteeShouldRedirectToListAfterDeletion() {
        Long committeeId = 1L;

        String viewName = committeeController.deleteCommittee(committeeId);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).deleteCommittee(committeeId);
    }

    @Test
    void assignTraineeshipToStudentShouldRedirectToListAfterAssignment() {
        Long committeeId = 1L;
        Long studentId = 2L;
        Long traineeshipId = 3L;

        String viewName = committeeController.assignTraineeshipToStudent(committeeId, studentId, traineeshipId);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).assignTraineeshipToStudent(committeeId, studentId, traineeshipId);
    }

    @Test
    void assignSupervisingProfessorShouldRedirectToListAfterAssignment() {
        Long committeeId = 1L;
        Long traineeshipId = 2L;
        Long professorId = 3L;

        String viewName = committeeController.assignSupervisingProfessor(committeeId, traineeshipId, professorId);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).assignSupervisingProfessor(committeeId, traineeshipId, professorId);
    }

    @Test
    void monitorTraineeshipEvaluationsShouldReturnMonitorEvaluationsView() {
        Long traineeshipId = 1L;

        Model model = mock(Model.class);
        String viewName = committeeController.monitorTraineeshipEvaluations(traineeshipId, model);

        assertEquals("committee/monitorEvaluations", viewName);
        verify(model).addAttribute("traineeshipId", traineeshipId);
    }
}
