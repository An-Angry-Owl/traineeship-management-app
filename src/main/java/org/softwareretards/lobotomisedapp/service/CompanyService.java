package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    CompanyDto createCompany(CompanyDto company);

    CompanyDto updateCompany(String username, CompanyDto company);

    void deleteCompany(String username);

    Optional<CompanyDto> findCompanyByUsername(String username);

    Optional<CompanyDto> findCompanyByName(String name);

    List<CompanyDto> findCompanyByLocation(String location);

    List<CompanyDto> getAllCompanies();

    List<TraineeshipPositionDto> getTraineeshipPositions(String username);

    List<TraineeshipPositionDto> getAssignedTraineeships(String username);

    TraineeshipPositionDto announceTraineeship(TraineeshipPositionDto traineeshipPositionDto);

    void deleteTraineeship(Long traineeshipId);

    Evaluation evaluateTrainee(Long traineeshipPositionId, Integer motivation, Integer effectiveness, Integer efficiency);

    Optional<CompanyDto> findCompanyById(Long id);

    TraineeshipPositionDto getTraineeshipPositionByIdAndCompany(Long positionId, String companyUsername);

    TraineeshipPositionDto updateTraineeshipPosition(TraineeshipPositionDto positionDto, String companyUsername);

    void deleteTraineeshipPosition(Long positionId, String companyUsername);
}
