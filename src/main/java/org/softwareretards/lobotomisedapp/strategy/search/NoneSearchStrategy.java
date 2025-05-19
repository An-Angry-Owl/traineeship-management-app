package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class NoneSearchStrategy extends AbstractSearchStrategy {

    private final ProfessorRepository professorRepository;
    private final TraineeshipPositionRepository positionRepository;

    @Autowired
    public NoneSearchStrategy(ProfessorRepository professorRepository,
                                       TraineeshipPositionRepository positionRepository) {
        this.professorRepository = professorRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public SearchType getType() {
        return SearchType.NONE;
    }

    @Override
    public List<ProfessorDto> searchProfessors(Long positionId) {
        Optional<TraineeshipPosition> positionOpt = positionRepository.findById(positionId);
        if (positionOpt.isEmpty()) return Collections.emptyList();

        List<Professor> professors = professorRepository.findAll();

        return professors.stream()
                .map(ProfessorMapper::toDto)
                .collect(Collectors.toList());
    }
}
