package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;

import java.util.List;

public interface TraineeshipSearchStrategy {
    List<ProfessorDto> searchProfessors(Long positionId);
}
