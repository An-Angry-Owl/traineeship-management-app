package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;

import java.util.List;

public interface SearchService {
    List<TraineeshipPositionDto> recommend(Long professorId, SearchType type);
}
