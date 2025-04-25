package org.softwareretards.lobotomisedapp.service.impl;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public ProfessorServiceImpl(ProfessorRepository professorRepository,
                                UserRepository userRepository,
                                TraineeshipPositionRepository traineeshipPositionRepository,
                                EvaluationRepository evaluationRepository) {
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    @Transactional
    public ProfessorDto saveProfile(ProfessorDto professorDto) {
        Professor professor = ProfessorMapper.toEntity(professorDto);
        Professor savedProfessor = professorRepository.save(professor);
        return ProfessorMapper.toDto(savedProfessor);
    }

    @Override
    public ProfessorDto getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PROFESSOR) {
            throw new RuntimeException("User is not a professor");
        }

        return professorRepository.findById(user.getId())
                .map(ProfessorMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Professor profile not found"));
    }

    @Override
    public List<TraineeshipPositionDto> getSupervisedPositions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PROFESSOR) {
            throw new RuntimeException("User is not a professor");
        }

        Professor professor = professorRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Professor profile not found"));

        return traineeshipPositionRepository.findByProfessor(professor).stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvaluationDto saveEvaluation(String username, Long positionId, EvaluationDto evaluationDto) {
        // Validate professor
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PROFESSOR) {
            throw new RuntimeException("User is not a professor");
        }

        // Validate position ownership
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        if (!position.getProfessor().getId().equals(user.getId())) {
            throw new RuntimeException("Professor does not supervise this position");
        }

        // Validate rating ranges (1-5)
        validateRatings(evaluationDto);

        // Create/update evaluation
        Evaluation evaluation = EvaluationMapper.toEntity(evaluationDto);
        evaluation.setTraineeshipPosition(position);

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Link evaluation to position
        position.setEvaluation(savedEvaluation);
        traineeshipPositionRepository.save(position);

        return EvaluationMapper.toDto(savedEvaluation);
    }

    private void validateRatings(EvaluationDto dto) {
        validateRating(dto.getProfessorMotivationRating());
        validateRating(dto.getProfessorEffectivenessRating());
        validateRating(dto.getProfessorEfficiencyRating());
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException("Invalid rating value. Must be between 1-5");
        }
    }
}