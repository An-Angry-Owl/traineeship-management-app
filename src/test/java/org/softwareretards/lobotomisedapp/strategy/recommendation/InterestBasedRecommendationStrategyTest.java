package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterestBasedRecommendationStrategyTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TraineeshipPositionRepository positionRepository;

    @InjectMocks
    private InterestBasedRecommendationStrategy strategy;

    @Test
    void recommendTraineeshipsShouldReturnMatchingPositionsBasedOnInterests() {
        Long studentId = 1L;
        Student student = new Student();
        student.setInterests("AI, Machine Learning");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics("AI, Data Science");
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertEquals(0, result.size());
    }

    @Test
    void recommendTraineeshipsShouldReturnEmptyListWhenNoMatchingInterests() {
        Long studentId = 1L;
        Student student = new Student();
        student.setInterests("AI, Machine Learning");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics("Finance, Marketing");
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
    void recommendTraineeshipsShouldHandleNullStudentInterestsGracefully() {
        Long studentId = 1L;
        Student student = new Student();
        student.setInterests(null);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics("AI, Machine Learning");
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeshipsShouldHandleNullPositionTopicsGracefully() {
        Long studentId = 1L;
        Student student = new Student();
        student.setInterests("AI, Machine Learning");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics(null);
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeshipsShouldReturnEmptyListWhenBothInterestsAndTopicsAreEmpty() {
        Long studentId = 1L;
        Student student = new Student();
        student.setInterests("");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTopics("");
        when(positionRepository.findAvailablePositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(studentId);

        assertTrue(result.isEmpty());
    }
}
