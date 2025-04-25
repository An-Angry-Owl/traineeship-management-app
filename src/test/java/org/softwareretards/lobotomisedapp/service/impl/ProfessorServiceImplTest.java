package org.softwareretards.lobotomisedapp.service.impl;

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
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private ProfessorServiceImpl professorService;

    @Test
    void saveProfileShouldSaveAndReturnProfessorDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("professorUser");

        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setUserDto(userDto);

        Professor professor = new Professor();
        professor.setUsername("professorUser");

        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        ProfessorDto result = professorService.saveProfile(professorDto);

        assertEquals("professorUser", result.getUserDto().getUsername());
    }

    @Test
    void getProfileShouldReturnProfessorDtoWhenUserIsProfessor() {
        User user = new User();
        user.setId(1L);
        user.setUsername("professorUser");
        user.setRole(Role.PROFESSOR);

        Professor professor = new Professor();
        professor.setId(1L);
        professor.setUsername("professorUser");

        when(userRepository.findByUsername("professorUser")).thenReturn(Optional.of(user));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        ProfessorDto result = professorService.getProfile("professorUser");

        assertEquals("professorUser", result.getUserDto().getUsername());
    }

    @Test
    void getProfileShouldThrowExceptionWhenUserIsNotProfessor() {
        User user = new User();
        user.setId(1L);
        user.setUsername("nonProfessorUser");
        user.setRole(Role.STUDENT);

        when(userRepository.findByUsername("nonProfessorUser")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> professorService.getProfile("nonProfessorUser"));
    }

    @Test
    void getSupervisedPositionsShouldReturnListOfTraineeshipPositionDtos() {
        User user = new User();
        user.setId(1L);
        user.setUsername("professorUser");
        user.setRole(Role.PROFESSOR);

        Professor professor = new Professor();
        professor.setId(1L);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(2L);

        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();

        when(userRepository.findByUsername("professorUser")).thenReturn(Optional.of(user));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(traineeshipPositionRepository.findByProfessor(professor)).thenReturn(List.of(position));

        try (var mockedMapper = mockStatic(TraineeshipPositionMapper.class)) {
            mockedMapper.when(() -> TraineeshipPositionMapper.toDto(position)).thenReturn(positionDto);

            List<TraineeshipPositionDto> result = professorService.getSupervisedPositions("professorUser");

            assertEquals(1, result.size());
            assertEquals(positionDto, result.get(0));
        }
    }

    @Test
    void saveEvaluationShouldSaveAndReturnEvaluationDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("professorUser");
        user.setRole(Role.PROFESSOR);

        Professor professor = new Professor();
        professor.setId(1L);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(2L);
        position.setProfessor(professor);

        EvaluationDto evaluationDto = new EvaluationDto();
        evaluationDto.setProfessorMotivationRating(5);
        evaluationDto.setProfessorEfficiencyRating(4);
        evaluationDto.setProfessorEffectivenessRating(2);

        Evaluation evaluation = new Evaluation();
        evaluation.setId(3L);

        when(userRepository.findByUsername("professorUser")).thenReturn(Optional.of(user));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(position));
        when(evaluationRepository.save(evaluation)).thenReturn(evaluation);

        try (var mockedMapper = mockStatic(EvaluationMapper.class)) {
            mockedMapper.when(() -> EvaluationMapper.toEntity(evaluationDto)).thenReturn(evaluation);
            mockedMapper.when(() -> EvaluationMapper.toDto(evaluation)).thenReturn(evaluationDto);

            EvaluationDto result = professorService.saveEvaluation("professorUser", 2L, evaluationDto);

            assertEquals(5, result.getProfessorMotivationRating());
        }
    }
}
