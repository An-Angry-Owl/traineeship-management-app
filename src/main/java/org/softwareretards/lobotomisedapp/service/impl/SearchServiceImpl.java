package org.softwareretards.lobotomisedapp.service.impl;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.strategy.search.SearchStrategyFactory;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final SearchStrategyFactory strategyFactory;

    @Autowired
    public SearchServiceImpl(SearchStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public List<TraineeshipPositionDto> recommend(Long professorId, SearchType type) {
        return strategyFactory.getStrategy(type).searchTraineeships(professorId);
    }
}
