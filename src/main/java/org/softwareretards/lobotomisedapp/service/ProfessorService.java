package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import java.util.List;

public interface ProfessorService {
    ProfessorDto saveProfile(ProfessorDto professorDto);
    List<TraineeshipPositionDto> getSupervisedPositions(String username);
    EvaluationDto saveEvaluation(String username, Long positionId, EvaluationDto evaluationDto);
}