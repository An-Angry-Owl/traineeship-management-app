package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;

import java.util.List;


public interface CompanyService {

    CompanyDto createCompany(CompanyDto company);

    CompanyDto updateCompany(Long companyId, CompanyDto companyDto);

    void deleteCompany(Long companyId);

    CompanyDto findCompanyById(Long companyId);

    List<CompanyDto> findCompanyByName(String name);

    List<CompanyDto> findCompanyByLocation(String location);

    List<CompanyDto> getAllCompanies();

    List<TraineeshipPositionDto> getTraineeshipPositions(Long companyId);

    List<TraineeshipPositionDto> getAssignedTraineeships(Long companyId, boolean assigned);

    TraineeshipPositionDto announceTraineeship(TraineeshipPositionDto traineeshipPositionDto);

    void deleteTraineeship(Long traineeshipId);

    Evaluation evaluateTrainee(Long traineeshipPositionId, Integer motivation, Integer effectiveness, Integer efficiency);
}
