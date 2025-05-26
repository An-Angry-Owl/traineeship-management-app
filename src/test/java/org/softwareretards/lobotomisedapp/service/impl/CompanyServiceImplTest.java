package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.CompanyMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.CompanyRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyDto companyDto;
    private TraineeshipPosition position;
    private Evaluation evaluation;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setUsername("company1");
        company.setCompanyName("Test Company");
        company.setLocation("New York");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("company1");
        userDto.setPassword("password");

        companyDto = new CompanyDto();
        companyDto.setUserDto(userDto);
        companyDto.setCompanyName("Test Company");
        companyDto.setLocation("New York");

        position = new TraineeshipPosition();
        position.setId(1L);
        position.setCompany(company);
        position.setStatus(TraineeshipStatus.CLOSED);

        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setId(1L);
        positionDto.setStatus(TraineeshipStatus.CLOSED);

        evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setTraineeshipPosition(position);
    }

    @Test
    void updateCompany_ByUsername_NotFound_ShouldThrowException() {
        when(companyRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                companyService.updateCompany("unknown", companyDto));
    }

    @Test
    void deleteCompany_ByUsername_ShouldDeleteCompany() {
        when(companyRepository.findByUsername("company1")).thenReturn(Optional.of(company));

        companyService.deleteCompany("company1");

        verify(companyRepository).delete(company);
    }

    @Test
    void deleteTraineeship_ShouldDeletePosition() {
        companyService.deleteTraineeship(1L);

        verify(traineeshipPositionRepository).deleteById(1L);
    }

    @Test
    void evaluateTrainee_ValidInput_ShouldReturnUsername() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.of(position));
        when(evaluationRepository.findByTraineeshipPositionId(1L))
                .thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(evaluation)).thenReturn(evaluation);

        String result = companyService.evaluateTrainee("company1", 1L, 4, 5, 3);

        assertEquals("company1", result);
        assertEquals(4, evaluation.getCompStdMotivationRating());
        verify(evaluationRepository).save(evaluation);
    }

    @Test
    void evaluateTrainee_NewEvaluation_ShouldCreateEvaluation() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.of(position));
        when(evaluationRepository.findByTraineeshipPositionId(1L))
                .thenReturn(Optional.empty());
        when(evaluationRepository.save(any(Evaluation.class))).thenReturn(evaluation);

        String result = companyService.evaluateTrainee("company1", 1L, 4, 5, 3);

        assertEquals("company1", result);
        verify(evaluationRepository).save(any(Evaluation.class));
    }

    @Test
    void evaluateTrainee_PositionNotFound_ShouldThrowException() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                companyService.evaluateTrainee("company1", 1L, 4, 5, 3));
    }

    @Test
    void getTraineeshipPositionByIdAndCompany_NotFound_ShouldThrowException() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                companyService.getTraineeshipPositionByIdAndCompany(1L, "company1"));
    }

    @Test
    void deleteTraineeshipPosition_ShouldDeletePosition() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.of(position));

        companyService.deleteTraineeshipPosition(1L, "company1");

        verify(traineeshipPositionRepository).delete(position);
    }

    @Test
    void deleteTraineeshipPosition_NotFound_ShouldThrowException() {
        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1"))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                companyService.deleteTraineeshipPosition(1L, "company1"));
    }

    @Test
    void announceTraineeshipShouldSetStatusClosedAndReturnDto() {
        TraineeshipPositionDto inputDto = new TraineeshipPositionDto();
        TraineeshipPosition entity = new TraineeshipPosition();
        entity.setStatus(TraineeshipStatus.CLOSED);
        TraineeshipPosition savedEntity = new TraineeshipPosition();
        savedEntity.setStatus(TraineeshipStatus.CLOSED);
        TraineeshipPositionDto outputDto = new TraineeshipPositionDto();
        outputDto.setStatus(TraineeshipStatus.CLOSED);

        try (var mockedMapper = mockStatic(TraineeshipPositionMapper.class)) {
            mockedMapper.when(() -> TraineeshipPositionMapper.toEntity(inputDto)).thenReturn(entity);
            mockedMapper.when(() -> TraineeshipPositionMapper.toDto(savedEntity)).thenReturn(outputDto);

            when(traineeshipPositionRepository.save(entity)).thenReturn(savedEntity);

            TraineeshipPositionDto result = companyService.announceTraineeship(inputDto);

            assertEquals(TraineeshipStatus.CLOSED, result.getStatus());
        }
    }

    @Test
    void updateTraineeshipPositionShouldUpdateAllowedFieldsAndReturnDto() {
        TraineeshipPositionDto inputDto = new TraineeshipPositionDto();
        inputDto.setId(1L);
        inputDto.setStatus(TraineeshipStatus.OPEN);

        TraineeshipPosition existing = new TraineeshipPosition();
        existing.setId(1L);
        existing.setStatus(TraineeshipStatus.CLOSED);

        TraineeshipPosition updated = new TraineeshipPosition();
        updated.setId(1L);
        updated.setStatus(TraineeshipStatus.OPEN);

        TraineeshipPositionDto outputDto = new TraineeshipPositionDto();
        outputDto.setId(1L);
        outputDto.setStatus(TraineeshipStatus.OPEN);

        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1")).thenReturn(Optional.of(existing));
        when(traineeshipPositionRepository.save(existing)).thenReturn(updated);

        try (var mockedMapper = mockStatic(TraineeshipPositionMapper.class)) {
            mockedMapper.when(() -> TraineeshipPositionMapper.toDto(updated)).thenReturn(outputDto);

            TraineeshipPositionDto result = companyService.updateTraineeshipPosition(inputDto, "company1");

            assertEquals(TraineeshipStatus.OPEN, result.getStatus());
        }
    }

    @Test
    void updateTraineeshipPositionShouldThrowWhenPositionNotFound() {
        TraineeshipPositionDto inputDto = new TraineeshipPositionDto();
        inputDto.setId(99L);

        when(traineeshipPositionRepository.findByIdAndCompanyUsername(99L, "company1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> companyService.updateTraineeshipPosition(inputDto, "company1"));
    }

    @Test
    void updateTraineeshipPositionShouldThrowWhenTryingToCloseWithAssignedStudentOrProfessor() {
        TraineeshipPositionDto inputDto = new TraineeshipPositionDto();
        inputDto.setId(1L);
        inputDto.setStatus(TraineeshipStatus.CLOSED);

        TraineeshipPosition existing = new TraineeshipPosition();
        existing.setId(1L);
        existing.setStatus(TraineeshipStatus.OPEN);
        existing.setStudent(new org.softwareretards.lobotomisedapp.entity.user.Student());

        when(traineeshipPositionRepository.findByIdAndCompanyUsername(1L, "company1")).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> companyService.updateTraineeshipPosition(inputDto, "company1"));
    }

}