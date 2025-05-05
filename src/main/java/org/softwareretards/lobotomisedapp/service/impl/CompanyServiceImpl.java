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

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = CompanyMapper.toEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.toDto(savedCompany);
    }

    @Override
    public Optional<CompanyDto> findCompanyByUsername(String username) {
        return companyRepository.findByUsername(username)
                .map(CompanyMapper::toDto);
    }

    @Override
    public CompanyDto updateCompany(Long companyId, CompanyDto companyDto) {
        Optional<Company> existingCompanyOpt = companyRepository.findById(companyId);
        if (existingCompanyOpt.isEmpty()) {
            throw new RuntimeException("Company not found with ID: " + companyId);
        }

        Company existingCompany = existingCompanyOpt.get();
        existingCompany.setCompanyName(companyDto.getCompanyName());
        existingCompany.setLocation(companyDto.getLocation());
        existingCompany.setUsername(companyDto.getUsername());
        existingCompany.setPassword(companyDto.getPassword());
        existingCompany.setRole(companyDto.getRole());
        existingCompany.setEnabled(companyDto.isEnabled());
        existingCompany.setCreatedAt(companyDto.getCreatedAt());
        existingCompany.setUpdatedAt(companyDto.getUpdatedAt());

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDto(updatedCompany);
    }

    @Override
    public void deleteCompany(Long companyId) {
        companyRepository.deleteById(companyId);
    }

    @Override
    public CompanyDto findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .map(CompanyMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
    }

    @Override
    public List<CompanyDto> findCompanyByName(String companyName) {
        return companyRepository.findByCompanyName(companyName)
                .stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDto> findCompanyByLocation(String location) {
        return companyRepository.findByLocation(location)
                .stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper::toDto)
                .toList();
    }

    @Override
    public List<TraineeshipPositionDto> getTraineeshipPositions(Long companyId) {
        return traineeshipPositionRepository.findByCompanyId(companyId)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TraineeshipPositionDto> getAssignedTraineeships(Long companyId, boolean assigned) {
        return traineeshipPositionRepository.findAvailableByCompany(companyId)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TraineeshipPositionDto announceTraineeship(TraineeshipPositionDto traineeshipPositionDto) {
        TraineeshipPosition traineeshipPosition = TraineeshipPositionMapper.toEntity(traineeshipPositionDto);
        TraineeshipPosition savedTraineeship = traineeshipPositionRepository.save(traineeshipPosition);
        return TraineeshipPositionMapper.toDto(savedTraineeship);
    }

    @Override
    @Transactional
    public void deleteTraineeship(Long traineeshipPositionId) {
        Optional<TraineeshipPosition> traineeshipOpt = traineeshipPositionRepository.findById(traineeshipPositionId);
        if (traineeshipOpt.isPresent()) {
            TraineeshipPosition traineeship = traineeshipOpt.get();
            traineeshipPositionRepository.delete(traineeship);
        } else {
            throw new RuntimeException("Traineeship position not found with ID: " + traineeshipPositionId);
        }
    }

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
