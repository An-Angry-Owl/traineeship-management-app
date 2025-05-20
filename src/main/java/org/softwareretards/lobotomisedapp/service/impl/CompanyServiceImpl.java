package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
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

    @Override
    public CompanyDto updateCompany(String username, CompanyDto company) {
        Optional<Company> existingCompanyOpt = companyRepository.findByUsername(username);
        if (existingCompanyOpt.isEmpty()) {
            throw new RuntimeException("Company not found with username: " + username);
        }

        Company existingCompany = existingCompanyOpt.get();
        existingCompany.setCompanyName(company.getCompanyName());
        existingCompany.setLocation(company.getLocation());

        Company updatedCompany = companyRepository.save(existingCompany);
        return CompanyMapper.toDto(updatedCompany);
    }

    @Override
    public void deleteCompany(String username) {
        Optional<Company> existingCompanyOpt = companyRepository.findByUsername(username);
        if (existingCompanyOpt.isEmpty()) {
            throw new RuntimeException("Company not found with username: " + username);
        }

        Company existingCompany = existingCompanyOpt.get();
        companyRepository.delete(existingCompany);
    }

    @Override
    public Optional<CompanyDto> findCompanyByUsername(String username) {
        return companyRepository.findByUsername(username)
                .map(CompanyMapper::toDto);
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

    public Optional<CompanyDto> findCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .map(CompanyMapper::toDto);
    }

    public Optional<CompanyDto> findCompanyByName(String companyName) {
        return companyRepository.findByCompanyName(companyName)
                .map(CompanyMapper::toDto);
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
    public List<TraineeshipPositionDto> getTraineeshipPositions(String username) {
        return traineeshipPositionRepository.findByCompanyUsername(username)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .toList();
    }


    // US9: Get list of assigned traineeships
    @Override
    public List<TraineeshipPositionDto> getAssignedTraineeships(String username) {
        return traineeshipPositionRepository.findByCompanyUsername(username)
                .stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    // US10: Announce a new traineeship position
    @Override
    @Transactional
    public TraineeshipPositionDto announceTraineeship(TraineeshipPositionDto traineeshipPositionDto) {
        TraineeshipPosition traineeship = TraineeshipPositionMapper.toEntity(traineeshipPositionDto);
        traineeship.setStatus(TraineeshipStatus.CLOSED);
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
    public String evaluateTrainee(String username, Long traineeshipPositionId,
                                  Integer motivation, Integer effectiveness, Integer efficiency) {
        // Verify traineeship belongs to the company
        TraineeshipPosition position = traineeshipPositionRepository
                .findByIdAndCompanyUsername(traineeshipPositionId, username)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Get or create evaluation
        Evaluation evaluation = evaluationRepository.findByTraineeshipPositionId(traineeshipPositionId)
                .orElseGet(() -> {
                    Evaluation newEvaluation = new Evaluation();
                    newEvaluation.setTraineeshipPosition(position); // Set required relationship
                    return newEvaluation;
                });

        // Set the student evaluation fields (company's perspective)
        evaluation.setCompStdMotivationRating(motivation);
        evaluation.setCompStdEffectivenessRating(effectiveness);
        evaluation.setCompStdEfficiencyRating(efficiency);

        // Save or update the evaluation
        evaluationRepository.save(evaluation);

        return username;
    }

    @Override
    public TraineeshipPositionDto getTraineeshipPositionByIdAndCompany(Long positionId, String companyUsername) {
        return traineeshipPositionRepository
                .findByIdAndCompanyUsername(positionId, companyUsername)
                .map(TraineeshipPositionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Position with ID %d not found for company %s", positionId, companyUsername)
                ));
    }

    @Override
    @Transactional
    public TraineeshipPositionDto updateTraineeshipPosition(TraineeshipPositionDto positionDto, String companyUsername) {
        // First verify position exists and belongs to company
        TraineeshipPosition existingPosition = traineeshipPositionRepository
                .findByIdAndCompanyUsername(positionDto.getId(), companyUsername)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Position with ID %d not found for company %s", positionDto.getId(), companyUsername)
                ));

        // Update only allowed fields
        existingPosition.setDescription(positionDto.getDescription());
        existingPosition.setRequiredSkills(positionDto.getRequiredSkills());
        existingPosition.setTopics(positionDto.getTopics());
        existingPosition.setStartDate(positionDto.getStartDate());
        existingPosition.setEndDate(positionDto.getEndDate());

        existingPosition.setDescription(positionDto.getDescription());
        existingPosition.setRequiredSkills(positionDto.getRequiredSkills());
        existingPosition.setTopics(positionDto.getTopics());
        existingPosition.setStartDate(positionDto.getStartDate());
        existingPosition.setEndDate(positionDto.getEndDate());


        if (positionDto.getStatus() == TraineeshipStatus.OPEN) {
            existingPosition.setStatus(TraineeshipStatus.OPEN);
        } else {
            if (existingPosition.getStudent() != null || existingPosition.getProfessor() != null) {
                throw new IllegalStateException("To close the position, it must be unassigned. Contact the committee for more information.");
            }
            existingPosition.setStatus(TraineeshipStatus.CLOSED);
        }

        TraineeshipPosition updatedPosition = traineeshipPositionRepository.save(existingPosition);
        return TraineeshipPositionMapper.toDto(updatedPosition);
    }

    @Override
    @Transactional
    public void deleteTraineeshipPosition(Long positionId, String companyUsername) {
        // Verify position exists and belongs to company before deletion
        TraineeshipPosition position = traineeshipPositionRepository
                .findByIdAndCompanyUsername(positionId, companyUsername)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Position with ID %d not found for company %s", positionId, companyUsername)
                ));

        traineeshipPositionRepository.delete(position);
    }

}
