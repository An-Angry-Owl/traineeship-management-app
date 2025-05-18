package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InterestBasedSearchStrategy extends AbstractSearchStrategy {

    private final ProfessorRepository professorRepository;
    private final TraineeshipPositionRepository positionRepository;

    private static final double JACCARD_THRESHOLD = 0.4;

    @Autowired
    public InterestBasedSearchStrategy(ProfessorRepository professorRepository,
                                       TraineeshipPositionRepository positionRepository) {
        this.professorRepository = professorRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public SearchType getType() {
        return SearchType.INTEREST_BASED;
    }

    @Override
    public List<TraineeshipPositionDto> searchTraineeships(Long professorId) {
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        if (professorOpt.isEmpty()) return Collections.emptyList();

        Professor professor = professorOpt.get();
        Set<String> professorInterests = parseAndNormalize(professor.getInterests());

        List<TraineeshipPosition> availablePositions = positionRepository.findAvailableProfessorPositions();

        List<TraineeshipPosition> matching = availablePositions.stream()
                .filter(pos -> {
                    Set<String> topics = parseAndNormalize(pos.getTopics());
                    double jaccard = calculateJaccard(professorInterests, topics);
                    return jaccard >= JACCARD_THRESHOLD;
                })
                .toList();
        return matching.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }
}

