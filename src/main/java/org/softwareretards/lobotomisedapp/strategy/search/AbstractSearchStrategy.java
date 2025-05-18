package org.softwareretards.lobotomisedapp.strategy.search;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSearchStrategy implements TraineeshipSearchStrategy {
    public abstract SearchType getType();

    @Override
    public abstract List<TraineeshipPositionDto> searchTraineeships(Long professorId);

    protected final Set<String> parseAndNormalize(String input) {
        if (input == null || input.isBlank()) return Collections.emptySet();
        return Arrays.stream(input.split("[,\\n]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    protected final double calculateJaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);

        return (double) intersection.size() / union.size();
    }

    protected final String normalize(String location) {
        return location == null ? "" : location.trim().toLowerCase();
    }
}
