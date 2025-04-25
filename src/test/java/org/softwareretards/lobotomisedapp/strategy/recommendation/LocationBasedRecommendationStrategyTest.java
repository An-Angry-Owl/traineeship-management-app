package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationBasedRecommendationStrategyTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TraineeshipPositionRepository positionRepository;

    @InjectMocks
    private LocationBasedRecommendationStrategy strategy;

    @Test
    void recommendTraineeshipsShouldReturnMatchingPositionsForPreferredLocation() {
        Long studentId = 1L;
        Student student = new Student();
        student.setPreferredLocation("New York");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Company company = new Company();
        company.setLocation("New York");
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompany(company);
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertEquals(1, result.size());
    }

    @Test
    void recommendTraineeshipsShouldReturnEmptyListWhenNoMatchingPositions() {
        Long studentId = 1L;
        Student student = new Student();
        student.setPreferredLocation("New York");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Company company = new Company();
        company.setLocation("Los Angeles");
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompany(company);
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeshipsShouldReturnEmptyListWhenStudentNotFound() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeshipsShouldHandleNullPreferredLocationGracefully() {
        Long studentId = 1L;
        Student student = new Student();
        student.setPreferredLocation(null);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Company company = new Company();
        company.setLocation("New York");
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompany(company);
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeshipsShouldHandleNullCompanyLocationGracefully() {
        Long studentId = 1L;
        Student student = new Student();
        student.setPreferredLocation("New York");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Company company = new Company();
        company.setLocation(null);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompany(company);
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }
}
