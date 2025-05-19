package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;

import java.util.List;

public interface SearchService {
    List<ProfessorDto> recommend(Long positionId, SearchType type);
}
