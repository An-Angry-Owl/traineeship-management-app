package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.CompanyMapper;
import org.softwareretards.lobotomisedapp.repository.user.CompanyRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, TraineeshipPositionRepository traineeshipRepository, EvaluationRepository evaluationRepository, TraineeshipPositionRepository traineeshipPositionRepository) {
        this.companyRepository = companyRepository;
        this.traineeshipPositionRepository = traineeshipRepository;
        this.evaluationRepository = evaluationRepository;
    }

    //US7 Create a Company-type User profile
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = CompanyMapper.toEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.toDto(savedCompany);
    }

    public CompanyDto updateCompany(Long companyId, CompanyDto companyDto) {
        Optional<Company> existingCompanyOpt = companyRepository.findById(companyId);
        if (existingCompanyOpt.isEmpty()) {
            throw new RuntimeException("Company not found with ID: " + companyId);
        }

        Company existingCompany = existingCompanyOpt.get();
        existingCompany.setCompanyName(companyDto.getCompanyName());
        existingCompany.setLocation(companyDto.getLocation());

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDto(updatedCompany);
    }

    public void deleteCompany(Long companyId) {
        companyRepository.deleteById(companyId);
    }

    // TODO: Possibly will be used in strategies thus determine and delete unnecessary
    public CompanyDto findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .map(CompanyMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    }

    public List<CompanyDto> findCompanyByName(String companyName) {
        return companyRepository.findByCompanyName(companyName)
                .stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> findCompanyByLocation(String location) {
        return companyRepository.findByLocation(location)
                .stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper::toDto)
                .toList();
    }


    // US8: Get list of advertised traineeships by a company
    @Override
    public List<TraineeshipPositionDto> getTraineeshipPositions(Long companyId) {
        return traineeshipPositionRepository.findByCompanyId(companyId)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }


    // US9: Get list of assigned traineeships
    @Override
    public List<TraineeshipPositionDto> getAssignedTraineeships(Long companyId, boolean assigned) {
        return traineeshipPositionRepository.findAvailableByCompany(companyId)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    // US10: Announce a new traineeship position
    @Override
    @Transactional
    public TraineeshipPositionDto announceTraineeship(TraineeshipPositionDto traineeshipPositionDto) {
        TraineeshipPosition traineeship = TraineeshipPositionMapper.toEntity(traineeshipPositionDto);
        TraineeshipPosition savedTraineeship = traineeshipPositionRepository.save(traineeship);
        return TraineeshipPositionMapper.toDto(savedTraineeship);
    }

    // US11: Delete a traineeship position
    @Override
    @Transactional
    public void deleteTraineeship(Long traineeshipId) {
        traineeshipPositionRepository.deleteById(traineeshipId);
    }

    // TODO: Check whether this is correct
    // US12: Fill in an evaluation for a traineeship (Company Side)
    @Override
    @Transactional
    public Evaluation evaluateTrainee(Long traineeshipPositionId, Integer motivation, Integer effectiveness, Integer efficiency) {
        Optional<Evaluation> evaluationOpt = evaluationRepository.findByTraineeshipPositionId(traineeshipPositionId);

        if (evaluationOpt.isEmpty()) {
            throw new RuntimeException("No evaluation found for this traineeship position.");
        }

        Evaluation evaluation = evaluationOpt.get();
        evaluation.setCompanyMotivationRating(motivation);
        evaluation.setCompanyEffectivenessRating(effectiveness);
        evaluation.setCompanyEfficiencyRating(efficiency);

        return evaluationRepository.save(evaluation);
    }
}
