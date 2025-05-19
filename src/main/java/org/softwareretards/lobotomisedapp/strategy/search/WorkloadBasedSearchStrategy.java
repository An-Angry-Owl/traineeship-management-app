package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
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
    public List<ProfessorDto> searchProfessors(Long positionId) {
        // We don't really use positionId for workload-based sorting, but it's part of the signature

        List<Professor> professors = professorRepository.findAll();

        professors.sort(Comparator.comparingInt(prof -> {
            Integer count = positionRepository.countPositionsByProfessorId(prof.getId());
            return count != null ? count : 0;
        }));

        return professors.stream()
                .map(ProfessorMapper::toDto)
                .collect(Collectors.toList());
    }
}
