package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.mapper.user.CommitteeMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.CommitteeRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommitteeServiceImplTest {

    @Mock
    private CommitteeRepository committeeRepository;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private CommitteeServiceImpl committeeService;

    @Test
    void getAllCommitteesShouldReturnListOfCommitteeDtos() {
        Committee committee = new Committee();
        CommitteeDto committeeDto = new CommitteeDto();

        when(committeeRepository.findAll()).thenReturn(List.of(committee));
        try (var mockedMapper = mockStatic(CommitteeMapper.class)) {
            mockedMapper.when(() -> CommitteeMapper.toDto(committee)).thenReturn(committeeDto);

            List<CommitteeDto> result = committeeService.getAllCommittees();

            assertEquals(1, result.size());
            assertEquals(committeeDto, result.get(0));
        }
    }

    @Test
    void getCommitteeByIdShouldReturnCommitteeDtoWhenFound() {
        Committee committee = new Committee();
        CommitteeDto committeeDto = new CommitteeDto();

        when(committeeRepository.findById(1L)).thenReturn(Optional.of(committee));
        try (var mockedMapper = mockStatic(CommitteeMapper.class)) {
            mockedMapper.when(() -> CommitteeMapper.toDto(committee)).thenReturn(committeeDto);

            CommitteeDto result = committeeService.getCommitteeById(1L);

            assertEquals(committeeDto, result);
        }
    }

    @Test
    void getCommitteeByIdShouldReturnNullWhenNotFound() {
        when(committeeRepository.findById(1L)).thenReturn(Optional.empty());

        CommitteeDto result = committeeService.getCommitteeById(1L);

        assertNull(result);
    }

    @Test
    void createCommitteeShouldSaveAndReturnCommitteeDto() {
        CommitteeDto committeeDto = new CommitteeDto();
        Committee committee = new Committee();

        try (var mockedMapper = mockStatic(CommitteeMapper.class)) {
            mockedMapper.when(() -> CommitteeMapper.toEntity(committeeDto)).thenReturn(committee);
            mockedMapper.when(() -> CommitteeMapper.toDto(committee)).thenReturn(committeeDto);

            when(committeeRepository.save(committee)).thenReturn(committee);

            CommitteeDto result = committeeService.createCommittee(committeeDto);

            assertEquals(committeeDto, result);
        }
    }

    @Test
    void updateCommitteeShouldUpdateAndReturnUpdatedCommitteeDto() {
        CommitteeDto committeeDto = new CommitteeDto();
        committeeDto.setCommitteeName("UpdatedName");
        committeeDto.setUserDto(new UserDto());
        committeeDto.getUserDto().setUsername("UpdatedUsername");

        Committee existingCommittee = new Committee();
        existingCommittee.setCommitteeName("OldName");
        existingCommittee.setUsername("OldUsername");

        Committee updatedCommittee = new Committee();
        updatedCommittee.setCommitteeName("UpdatedName");
        updatedCommittee.setUsername("UpdatedUsername");

        when(committeeRepository.findById(1L)).thenReturn(Optional.of(existingCommittee));
        when(committeeRepository.save(existingCommittee)).thenReturn(updatedCommittee);

        try (var mockedMapper = mockStatic(CommitteeMapper.class)) {
            mockedMapper.when(() -> CommitteeMapper.toDto(updatedCommittee)).thenReturn(committeeDto);

            CommitteeDto result = committeeService.updateCommittee(1L, committeeDto);

            assertEquals("UpdatedName", result.getCommitteeName());
            assertEquals("UpdatedUsername", result.getUserDto().getUsername());
        }
    }

    @Test
    void updateCommitteeShouldReturnNullWhenCommitteeNotFound() {
        when(committeeRepository.findById(1L)).thenReturn(Optional.empty());

        CommitteeDto committeeDto = new CommitteeDto();

        CommitteeDto result = committeeService.updateCommittee(1L, committeeDto);

        assertNull(result);
    }

    @Test
    void deleteCommitteeShouldDeleteCommitteeById() {
        doNothing().when(committeeRepository).deleteById(1L);

        committeeService.deleteCommittee(1L);

        verify(committeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void assignTraineeshipToStudentShouldAssignWhenEntitiesExist() {
        Student student = new Student();
        TraineeshipPosition traineeship = new TraineeshipPosition();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(traineeship));
        when(traineeshipPositionRepository.save(traineeship)).thenReturn(traineeship);

        committeeService.assignTraineeshipToStudent(1L, 1L, 2L);

        assertEquals(student, traineeship.getStudent());
        verify(traineeshipPositionRepository, times(1)).save(traineeship);
    }

    @Test
    void assignTraineeshipToStudentShouldThrowExceptionWhenEntitiesNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> committeeService.assignTraineeshipToStudent(1L, 1L, 2L));
    }

    @Test
    void assignSupervisingProfessorShouldAssignWhenEntitiesExist() {
        Professor professor = new Professor();
        TraineeshipPosition traineeship = new TraineeshipPosition();

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(traineeship));
        when(traineeshipPositionRepository.save(traineeship)).thenReturn(traineeship);

        committeeService.assignSupervisingProfessor(1L, 2L, 1L);

        assertEquals(professor, traineeship.getProfessor());
        verify(traineeshipPositionRepository, times(1)).save(traineeship);
    }

    @Test
    void assignSupervisingProfessorShouldThrowExceptionWhenEntitiesNotFound() {
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> committeeService.assignSupervisingProfessor(1L, 2L, 1L));
    }

    @Test
    void monitorTraineeshipEvaluationsShouldPrintEvaluationWhenExists() {
        Evaluation evaluation = new Evaluation();
        evaluation.setFinalMark(FinalMark.PASS);
        TraineeshipPosition traineeship = new TraineeshipPosition();
        traineeship.setEvaluation(evaluation);

        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(traineeship));

        committeeService.monitorTraineeshipEvaluations(1L, 1L);

        verify(traineeshipPositionRepository, times(1)).findById(1L);
    }

    @Test
    void monitorTraineeshipEvaluationsShouldThrowExceptionWhenTraineeshipNotFound() {
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> committeeService.monitorTraineeshipEvaluations(1L, 1L));
    }
}
