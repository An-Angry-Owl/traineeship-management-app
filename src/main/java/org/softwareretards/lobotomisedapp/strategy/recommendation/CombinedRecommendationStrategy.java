package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CombinedRecommendationStrategy extends AbstractRecommendationsStrategy {

    private final StudentRepository studentRepository;
    private final TraineeshipPositionRepository positionRepository;

    private static final double JACCARD_THRESHOLD = 0.4;

    @Autowired
    public CombinedRecommendationStrategy(StudentRepository studentRepository,
                                          TraineeshipPositionRepository positionRepository) {
        this.studentRepository = studentRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public RecommendationType getType() {
        return RecommendationType.COMBINED;
    }

    @Override
    public List<TraineeshipPositionDto> recommendTraineeships(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) return Collections.emptyList();

        Student student = studentOpt.get();
        String preferredLocation = normalize(student.getPreferredLocation());
        Set<String> interests = parseAndNormalize(student.getInterests());

        // Step 1: Get positions that match the preferred location
        List<TraineeshipPosition> locationMatches = positionRepository.findByAvailability(true).stream()
                .filter(pos -> {
                    Company company = pos.getCompany();
                    if (company == null || company.getLocation() == null) return false;
                    return preferredLocation.equals(normalize(company.getLocation()));
                })
                .toList();

        // Step 2: Filter by Jaccard similarity
        List<TraineeshipPosition> matching = locationMatches.stream()
                .filter(pos -> {
                    Set<String> topics = parseAndNormalize(pos.getTopics());
                    double jaccard = calculateJaccard(interests, topics);
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

    private String normalize(String location) {
        return location == null ? "" : location.trim().toLowerCase();
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
