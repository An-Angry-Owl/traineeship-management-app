package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InterestBasedRecommendationStrategy extends AbstractRecommendationsStrategy {

    private final StudentRepository studentRepository;
    private final TraineeshipPositionRepository positionRepository;

    private static final double JACCARD_THRESHOLD = 0.4;

    @Autowired
    public InterestBasedRecommendationStrategy(StudentRepository studentRepository,
                                               TraineeshipPositionRepository positionRepository) {
        this.studentRepository = studentRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public RecommendationType getType() {
        return RecommendationType.INTEREST_BASED;
    }

    @Override
    public List<TraineeshipPositionDto> recommendTraineeships(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) return Collections.emptyList();

        Student student = studentOpt.get();
        Set<String> studentInterests = parseAndNormalize(student.getInterests());

        List<TraineeshipPosition> availablePositions = positionRepository.findByAvailability(true);

        List<TraineeshipPosition> matching = availablePositions.stream()
                .filter(pos -> {
                    Set<String> topics = parseAndNormalize(pos.getTopics());
                    double jaccard = calculateJaccard(studentInterests, topics);
                    return jaccard >= JACCARD_THRESHOLD;
                })
                .toList();

        return matching.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    private Set<String> parseAndNormalize(String input) {
        if (input == null || input.isBlank()) return Collections.emptySet();
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private double calculateJaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);

        return (double) intersection.size() / union.size();
    }
}