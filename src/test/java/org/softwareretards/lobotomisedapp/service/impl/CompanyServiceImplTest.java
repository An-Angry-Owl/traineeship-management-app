package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
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
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private EvaluationRepository evaluationRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void createCompanyShouldSaveAndReturnCompanyDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("TechCorp");
        companyDto.setUserDto(userDto);

        Company company = new Company();
        company.setCompanyName("TechCorp");

        when(companyRepository.save(any(Company.class))).thenReturn(company);

        try (var mockedMapper = mockStatic(CompanyMapper.class)) {
            mockedMapper.when(() -> CompanyMapper.toEntity(companyDto)).thenReturn(company);
            mockedMapper.when(() -> CompanyMapper.toDto(company)).thenReturn(companyDto);

            CompanyDto result = companyService.createCompany(companyDto);

            assertEquals("TechCorp", result.getCompanyName());
        }
    }

    @Test
    void updateCompanyShouldUpdateAndReturnUpdatedCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("UpdatedCorp");
        companyDto.setLocation("New York");

        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setCompanyName("OldCorp");
        existingCompany.setLocation("Los Angeles");

        Company updatedCompany = new Company();
        updatedCompany.setId(1L);
        updatedCompany.setCompanyName("UpdatedCorp");
        updatedCompany.setLocation("New York");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany)).thenReturn(updatedCompany);

        try (var mockedMapper = mockStatic(CompanyMapper.class)) {
            mockedMapper.when(() -> CompanyMapper.toDto(updatedCompany)).thenReturn(companyDto);

            CompanyDto result = companyService.updateCompany(1L, companyDto);

            assertEquals("UpdatedCorp", result.getCompanyName());
            assertEquals("New York", result.getLocation());
        }
    }

    @Test
    void updateCompanyShouldThrowExceptionWhenCompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("NonExistentCorp");

        assertThrows(RuntimeException.class, () -> companyService.updateCompany(1L, companyDto));
    }

    @Test
    void getTraineeshipPositionsShouldReturnListOfTraineeshipPositionDtos() {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);

        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();

        when(traineeshipPositionRepository.findByCompanyId(1L)).thenReturn(List.of(position));
        try (var mockedMapper = mockStatic(TraineeshipPositionMapper.class)) {
            mockedMapper.when(() -> TraineeshipPositionMapper.toDto(position)).thenReturn(positionDto);

            List<TraineeshipPositionDto> result = companyService.getTraineeshipPositions(1L);

            assertEquals(1, result.size());
            assertEquals(positionDto, result.get(0));
        }
    }

    @Test
    void evaluateTraineeShouldUpdateAndReturnEvaluation() {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);

        when(evaluationRepository.findByTraineeshipPositionId(1L)).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(evaluation)).thenReturn(evaluation);

        Evaluation result = companyService.evaluateTrainee(1L, 5, 4, 3);

        assertEquals(5, result.getCompanyMotivationRating());
        assertEquals(4, result.getCompanyEffectivenessRating());
        assertEquals(3, result.getCompanyEfficiencyRating());
    }

    @Test
    void evaluateTraineeShouldThrowExceptionWhenEvaluationNotFound() {
        when(evaluationRepository.findByTraineeshipPositionId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> companyService.evaluateTrainee(1L, 5, 4, 3));
    }
}
