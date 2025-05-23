package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestBasedRecommendationStrategyTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TraineeshipPositionRepository positionRepository;

    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @InjectMocks
    private InterestBasedRecommendationStrategy strategy;

    private Student student;
    private TraineeshipPosition position1;
    private TraineeshipPosition position2;
    private TraineeshipPositionDto positionDto1;
    private TraineeshipPositionDto positionDto2;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setInterests("java,spring,backend");
        student.setSkills("programming,oop,git");

        position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setTopics("java,spring");
        position1.setRequiredSkills("programming,oop");

        position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setTopics("python,django");
        position2.setRequiredSkills("programming");

        positionDto1 = new TraineeshipPositionDto();
        positionDto1.setId(1L);

        positionDto2 = new TraineeshipPositionDto();
        positionDto2.setId(2L);
    }

    @Test
    void getType_ShouldReturnInterestBased() {
        assertEquals(RecommendationType.INTEREST_BASED, strategy.getType());
    }

    @Test
    void recommendTraineeships_StudentNotFound_ShouldReturnEmptyList() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_NoAvailablePositions_ShouldReturnEmptyList() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(Collections.emptyList());

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_LowJaccardSimilarity_ShouldReturnEmptyList() {
        position1.setTopics("python,machine learning");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1, position2));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_MissingRequiredSkills_ShouldReturnEmptyList() {
        position1.setRequiredSkills("advanced-math,physics");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1, position2));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_EmptySkills_ShouldNotMatchAnyRequiredSkills() {
        student.setSkills("");
        position1.setRequiredSkills("programming");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_EmptyTopics_ShouldResultInZeroJaccard() {
        position1.setTopics("");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

}