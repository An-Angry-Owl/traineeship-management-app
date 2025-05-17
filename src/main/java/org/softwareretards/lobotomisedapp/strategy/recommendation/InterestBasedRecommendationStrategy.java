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
        Set<String> studentSkills = parseAndNormalize(student.getSkills());

        List<TraineeshipPosition> availablePositions = positionRepository.findAvailablePositions();

        List<TraineeshipPosition> matching = availablePositions.stream()
                .filter(pos -> {
                    Set<String> topics = parseAndNormalize(pos.getTopics());
                    Set<String> requiredSkills = parseAndNormalize(pos.getRequiredSkills());
                    double jaccard = calculateJaccard(studentInterests, topics);
                    boolean hasAllRequiredSkills = studentSkills.containsAll(requiredSkills);
                    return jaccard >= JACCARD_THRESHOLD && hasAllRequiredSkills;
                })
                .toList();


        return matching.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }


}