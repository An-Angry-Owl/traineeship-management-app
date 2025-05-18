package org.softwareretards.lobotomisedapp.strategy.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchStrategyFactory {

    private final Map<SearchType, AbstractSearchStrategy> strategyMap = new EnumMap<>(SearchType.class);

    @Autowired
    public SearchStrategyFactory(List<TraineeshipSearchStrategy> strategies) {
        for (TraineeshipSearchStrategy strategy : strategies) {
            if (strategy instanceof AbstractSearchStrategy typedStrategy) {
                strategyMap.put(typedStrategy.getType(), typedStrategy);
            } else {
                throw new IllegalArgumentException("Strategy does not extend AbstractSearchStrategy");
            }
        }
    }

    public AbstractSearchStrategy getStrategy(SearchType type) {
        AbstractSearchStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }
        return strategy;
    }
}
