package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
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
    public List<ProfessorDto> searchProfessors(Long positionId) {
        Optional<TraineeshipPosition> positionOpt = positionRepository.findById(positionId);
        if (positionOpt.isEmpty()) return Collections.emptyList();

        TraineeshipPosition position = positionOpt.get();
        Set<String> positionTopics = parseAndNormalize(position.getTopics());

        List<Professor> allProfessors = professorRepository.findAll();

        List<Professor> matching = allProfessors.stream()
                .filter(prof -> {
                    Set<String> interests = parseAndNormalize(prof.getInterests());
                    double jaccard = calculateJaccard(positionTopics, interests);
                    return jaccard >= JACCARD_THRESHOLD;
                })
                .collect(Collectors.toList());

        return matching.stream()
                .map(ProfessorMapper::toDto)
                .collect(Collectors.toList());
    }
}

