package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RecommendationsStrategyFactory {

    private final Map<RecommendationType, AbstractRecommendationsStrategy> strategyMap = new EnumMap<>(RecommendationType.class);

    @Autowired
    public RecommendationsStrategyFactory(List<TraineeshipRecommendationsStrategy> strategies) {
        for (TraineeshipRecommendationsStrategy strategy : strategies) {
            if (strategy instanceof AbstractRecommendationsStrategy typedStrategy) {
                strategyMap.put(typedStrategy.getType(), typedStrategy);
            } else {
                throw new IllegalArgumentException("Strategy does not extend AbstractRecommendationsStrategy");
            }
        }
    }

    public AbstractRecommendationsStrategy getStrategy(RecommendationType type) {
        AbstractRecommendationsStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + type);
        }
        return strategy;
    }
}