package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractRecommendationsStrategy implements TraineeshipRecommendationsStrategy {
    public abstract RecommendationType getType();

    @Override
    public abstract List<TraineeshipPositionDto> recommendTraineeships(Long studentId);

    protected final Set<String> parseAndNormalize(String input) {
        if (input == null || input.isBlank()) return Collections.emptySet();
        return Arrays.stream(input.split("[,\\n]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}