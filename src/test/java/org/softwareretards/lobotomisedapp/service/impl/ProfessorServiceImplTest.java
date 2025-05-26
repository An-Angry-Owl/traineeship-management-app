package org.softwareretards.lobotomisedapp.service.impl;

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
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.mapper.user.UserMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private ProfessorMapper professorMapper;

    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @Mock
    private EvaluationMapper evaluationMapper;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    private Professor professor;
    private User user;
    private ProfessorDto professorDto;
    private TraineeshipPosition position;
    private EvaluationDto evaluationDto;
    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("prof1");
        user.setRole(Role.PROFESSOR);

        UserDto userDto = UserMapper.toDto(user);

        professor = new Professor();
        professor.setId(1L);
        professor.setId(1L);
        professor.setProfessorName("Professor Name");

        professorDto = new ProfessorDto();
        professorDto.setUserDto(userDto);
        professorDto.setFullName("Professor Name");
        professorDto.setInterests("research,teaching");

        position = new TraineeshipPosition();
        position.setId(1L);
        position.setProfessor(professor);

        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setTraineeshipPosition(position);
        evaluation.setFinalMark(FinalMark.PENDING);

        evaluationDto = new EvaluationDto();
        evaluationDto.setId(1L);
        evaluationDto.setFinalMark(FinalMark.PASS);
        evaluationDto.setProfStdMotivationRating(4);
        evaluationDto.setProfStdEffectivenessRating(5);
        evaluationDto.setProfStdEfficiencyRating(3);
        evaluationDto.setProfCompFacilitiesRating(4);
        evaluationDto.setProfCompGuidanceRating(5);
    }

    @Test
    void saveProfile_ProfessorNotFound_ShouldThrowException() {
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> professorService.saveProfile(professorDto));
    }

    @Test
    void getProfile_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> professorService.getProfile("prof1"));
    }

    @Test
    void getProfile_UserNotProfessor_ShouldThrowException() {
        user.setRole(Role.STUDENT);
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> professorService.getProfile("student1"));
    }

    @Test
    void getProfile_ProfessorProfileNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> professorService.getProfile("prof1"));
    }

    @Test
    void saveEvaluation_InvalidRating_ShouldThrowException() {
        evaluationDto.setProfStdMotivationRating(6);

        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));

        assertThrows(RuntimeException.class,
                () -> professorService.saveEvaluation("prof1", 1L, evaluationDto));
    }

    @Test
    void saveEvaluation_NotSupervisingProfessor_ShouldThrowException() {
        User otherProfessor = new User();
        otherProfessor.setId(2L);
        otherProfessor.setRole(Role.PROFESSOR);

        TraineeshipPosition otherPosition = new TraineeshipPosition();
        otherPosition.setId(2L);
        otherPosition.setProfessor(new Professor());

        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(otherPosition));

        assertThrows(RuntimeException.class,
                () -> professorService.saveEvaluation("prof1", 2L, evaluationDto));
    }

    @Test
    void saveEvaluation_MissingRequiredFields_ShouldThrowException() {
        evaluationDto.setProfStdMotivationRating(null);

        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));

        assertThrows(RuntimeException.class,
                () -> professorService.saveEvaluation("prof1", 1L, evaluationDto));
    }

    @Test
    void saveEvaluation_PositionNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> professorService.saveEvaluation("prof1", 1L, evaluationDto));
    }

    @Test
    void getProfile_ReturnsProfessorDtoWhenFound() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        try (var mockedMapper = mockStatic(ProfessorMapper.class)) {
            mockedMapper.when(() -> ProfessorMapper.toDto(professor)).thenReturn(professorDto);

            ProfessorDto result = professorService.getProfile("prof1");

            assertEquals("Professor Name", result.getFullName());
        }
    }

    @Test
    void getSupervisedPositions_ReturnsListOfDtos() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(traineeshipPositionRepository.findByProfessor(professor)).thenReturn(List.of(position));
        try (var mockedMapper = mockStatic(TraineeshipPositionMapper.class)) {
            TraineeshipPositionDto dto = new TraineeshipPositionDto();
            dto.setId(1L);
            mockedMapper.when(() -> TraineeshipPositionMapper.toDto(position)).thenReturn(dto);

            List<TraineeshipPositionDto> result = professorService.getSupervisedPositions("prof1");

            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
        }
    }

    @Test
    void saveEvaluation_CreatesNewEvaluationIfNotExists() {
        when(userRepository.findByUsername("prof1")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(evaluationRepository.findByTraineeshipPositionId(1L)).thenReturn(Optional.empty());
        when(evaluationRepository.save(any(Evaluation.class))).thenAnswer(invocation -> {
            Evaluation eval = invocation.getArgument(0);
            eval.setId(99L);
            return eval;
        });
        when(traineeshipPositionRepository.save(any(TraineeshipPosition.class))).thenReturn(position);
        try (var mockedMapper = mockStatic(EvaluationMapper.class)) {
            mockedMapper.when(() -> EvaluationMapper.toDto(any(Evaluation.class))).thenReturn(evaluationDto);

            EvaluationDto result = professorService.saveEvaluation("prof1", 1L, evaluationDto);

            assertEquals(FinalMark.PASS, result.getFinalMark());
        }
    }

    @Test
    void getOrCreateEvaluation_ReturnsNewEvaluationDtoIfNotExists() {
        when(evaluationRepository.findByTraineeshipPositionId(1L)).thenReturn(Optional.empty());

        EvaluationDto result = professorService.getOrCreateEvaluation(1L);

        assertNull(result.getId());
    }

}