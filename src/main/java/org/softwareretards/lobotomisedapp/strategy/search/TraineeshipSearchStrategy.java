package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;

import java.util.List;

public interface TraineeshipSearchStrategy {
    List<TraineeshipPositionDto> searchTraineeships(Long professorId);
}
