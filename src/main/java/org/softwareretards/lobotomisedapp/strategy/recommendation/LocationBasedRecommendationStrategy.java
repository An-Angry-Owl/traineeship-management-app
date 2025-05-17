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
public class LocationBasedRecommendationStrategy extends AbstractRecommendationsStrategy {

    private final StudentRepository studentRepository;
    private final TraineeshipPositionRepository positionRepository;

    @Autowired
    public LocationBasedRecommendationStrategy(StudentRepository studentRepository,
                                               TraineeshipPositionRepository positionRepository) {
        this.studentRepository = studentRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public RecommendationType getType() {
        return RecommendationType.LOCATION_BASED;
    }

    @Override
    public List<TraineeshipPositionDto> recommendTraineeships(Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) return Collections.emptyList();

        Student student = studentOpt.get();
        String preferredLocation = normalize(student.getPreferredLocation());
        Set<String> studentSkills = parseAndNormalize(student.getSkills());

        List<TraineeshipPosition> availablePositions = positionRepository.findAvailablePositions();

        List<TraineeshipPosition> matching = availablePositions.stream()
                .filter(pos -> {
                    Company company = pos.getCompany();
                    if (company == null || company.getLocation() == null) return false;
                    String companyLocation = normalize(company.getLocation());
                    Set<String> requiredSkills = parseAndNormalize(pos.getRequiredSkills());
                    boolean hasAllRequiredSkills = studentSkills.containsAll(requiredSkills);
                    return preferredLocation.equals(companyLocation) && hasAllRequiredSkills;
                })
                .toList();

        return matching.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    private String normalize(String location) {
        return location == null ? "" : location.trim().toLowerCase();
    }
}
