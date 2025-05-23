package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeControllerTest {

    @Mock
    private CommitteeService committeeService;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationsService recommendationsService;

    @Mock
    private TraineeshipApplicationRepository traineeshipApplicationRepository;

    @Mock
    private SearchService searchService;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommitteeController committeeController;

    private CommitteeDto committeeDto;
    private User user;
    private TraineeshipPositionDto positionDto;
    private Evaluation evaluation;
    private EvaluationDto evaluationDto;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("committee1");
        userDto.setPassword("password");

        committeeDto = new CommitteeDto();
        committeeDto.setUserDto(userDto);

        user = new User();
        user.setId(1L);
        user.setUsername("committee1");

        positionDto = new TraineeshipPositionDto();
        positionDto.setId(1L);

        evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setFinalMark(FinalMark.PENDING);

        evaluationDto = new EvaluationDto();
        evaluationDto.setId(1L);
        evaluationDto.setFinalMark(FinalMark.PENDING);
    }

    @Test
    void getAllCommittees_ShouldReturnViewWithCommittees() {
        when(committeeService.getAllCommittees()).thenReturn(Collections.singletonList(committeeDto));

        String viewName = committeeController.getAllCommittees(model);

        assertEquals("committee/list", viewName);
        verify(model).addAttribute("committees", Collections.singletonList(committeeDto));
    }

    @Test
    void getCommitteeById_ShouldReturnViewWithCommittee() {
        when(committeeService.getCommitteeById(1L)).thenReturn(committeeDto);

        String viewName = committeeController.getCommitteeById(1L, model);

        assertEquals("committee/view", viewName);
        verify(model).addAttribute("committee", committeeDto);
    }

    @Test
    void showCreateForm_ShouldReturnCreateView() {
        String viewName = committeeController.showCreateForm(model);

        assertEquals("committee/create", viewName);
        verify(model).addAttribute(eq("committeeDto"), any(CommitteeDto.class));
    }

    @Test
    void createCommittee_ShouldRedirectToList() {
        String viewName = committeeController.createCommittee(committeeDto);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).createCommittee(committeeDto);
    }

    @Test
    void showUpdateForm_ShouldReturnEditView() {
        when(committeeService.getCommitteeById(1L)).thenReturn(committeeDto);

        String viewName = committeeController.showUpdateForm(1L, model);

        assertEquals("committee/edit", viewName);
        verify(model).addAttribute("committeeDto", committeeDto);
    }

    @Test
    void updateCommittee_ShouldRedirectToList() {
        String viewName = committeeController.updateCommittee(1L, committeeDto);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).updateCommittee(1L, committeeDto);
    }

    @Test
    void deleteCommittee_ShouldRedirectToList() {
        String viewName = committeeController.deleteCommittee(1L);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).deleteCommittee(1L);
    }

    @Test
    void assignTraineeshipToStudent_ShouldRedirectToList() {
        String viewName = committeeController.assignTraineeshipToStudent(1L, 1L, 1L);

        assertEquals("redirect:/committee/list", viewName);
        verify(committeeService).assignTraineeshipToStudent(1L, 1L, 1L);
    }

    @Test
    void assignSupervisingProfessor_Failure_ShouldRedirectWithErrorMessage() {
        RuntimeException exception = new RuntimeException("Error message");
        doThrow(exception).when(committeeService).assignSupervisingProfessor(1L, 1L, 1L);

        String viewName = committeeController.assignSupervisingProfessor(1L, 1L, 1L, redirectAttributes);

        assertEquals("redirect:/committees/professor-list?professorId=1", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Error message");
    }

    @Test
    void monitorTraineeshipEvaluations_ShouldReturnMonitorView() {
        String viewName = committeeController.monitorTraineeshipEvaluations(1L, model);

        assertEquals("committee/monitorEvaluations", viewName);
        verify(model).addAttribute("traineeshipId", 1L);
    }

    @Test
    void showDashboard_Unauthorized_ShouldRedirectToAccessDenied() {
        User differentUser = new User();
        differentUser.setUsername("differentUser");

        String viewName = committeeController.showDashboard("committee1", differentUser, model);

        assertEquals("redirect:/access-denied", viewName);
    }

    @Test
    void showStudentList_WithStudentId_ShouldReturnStudentListView() {
        when(authentication.getName()).thenReturn("committee1");
        when(userRepository.findByUsername("committee1")).thenReturn(Optional.of(user));
        when(committeeService.getCommitteeById(1L)).thenReturn(committeeDto);
        when(traineeshipApplicationRepository.findDistinctStudentIds()).thenReturn(Collections.singletonList(1L));
        when(studentRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        when(recommendationsService.recommend(1L, RecommendationType.NONE)).thenReturn(Collections.emptyList());

        String viewName = committeeController.showStudentList(1L, RecommendationType.NONE, model, authentication);

        assertEquals("committees/student-list", viewName);
        verify(model).addAttribute("committee", committeeDto);
        verify(model).addAttribute("students", Collections.emptyList());
    }

    @Test
    void showProfileForm_ShouldReturnProfileFormView() {
        when(userRepository.findByUsername("committee1")).thenReturn(Optional.of(user));
        when(committeeService.getCommitteeById(1L)).thenReturn(committeeDto);

        String viewName = committeeController.showProfileForm("committee1", model);

        assertEquals("committees/profile-form", viewName);
        verify(model).addAttribute("committee", committeeDto);
    }

    @Test
    void saveProfile_ShouldRedirectToProfile() {
        when(userRepository.findByUsername("committee1")).thenReturn(Optional.of(user));
        when(committeeService.getCommitteeById(1L)).thenReturn(committeeDto);
        when(committeeService.updateCommittee(anyLong(), any())).thenReturn(committeeDto);

        String viewName = committeeController.saveProfile("committee1", committeeDto, model);

        assertEquals("redirect:/committees/committee1/profile", viewName);
        verify(model).addAttribute("committee", committeeDto);
    }

    @Test
    void changePassword_Success_ShouldRedirectWithSuccessMessage() {
        String viewName = committeeController.changePassword(
                "committee1", "oldPass", "newPass", "newPass", redirectAttributes);

        assertEquals("redirect:/committees/committee1/profile", viewName);
        verify(userService).changePassword("committee1", "oldPass", "newPass", "newPass");
        verify(redirectAttributes).addFlashAttribute("success", "Password changed successfully!");
    }

    @Test
    void changePassword_Failure_ShouldRedirectWithErrorMessage() {
        RuntimeException exception = new RuntimeException("Password error");
        doThrow(exception).when(userService).changePassword("committee1", "oldPass", "newPass", "newPass");

        String viewName = committeeController.changePassword(
                "committee1", "oldPass", "newPass", "newPass", redirectAttributes);

        assertEquals("redirect:/committees/committee1/profile", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Password error");
    }

    @Test
    void assignTraineeship_Success_ShouldRedirectWithSuccessMessage() {
        String viewName = committeeController.assignTraineeship(1L, 1L, 1L, redirectAttributes);

        assertEquals("redirect:/committees/student-list?studentId=1", viewName);
        verify(committeeService).assignTraineeshipToStudent(1L, 1L, 1L);
        verify(committeeService).acceptApplication(1L);
        verify(redirectAttributes).addFlashAttribute("success", "Traineeship assigned successfully");
    }

}