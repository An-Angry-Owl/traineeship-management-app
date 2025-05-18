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
public class WorkloadBasedSearchStrategy extends AbstractSearchStrategy {

    private final ProfessorRepository professorRepository;
    private final TraineeshipPositionRepository positionRepository;

    @Autowired
    public WorkloadBasedSearchStrategy(ProfessorRepository professorRepository,
                                       TraineeshipPositionRepository positionRepository) {
        this.professorRepository = professorRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public SearchType getType() {
        return SearchType.WORKLOAD;
    }

    @Override
    public List<TraineeshipPositionDto> searchTraineeships(Long professorId) {
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        if (professorOpt.isEmpty()) return Collections.emptyList();

        List<TraineeshipPosition> positionsSortedByWorkload = positionRepository.findAvailablePositionsOrderByProfessorWorkload();

        return positionsSortedByWorkload.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }
}
