package org.softwareretards.lobotomisedapp.strategy.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
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
class CombinedRecommendationStrategyTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TraineeshipPositionRepository positionRepository;

    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @InjectMocks
    private CombinedRecommendationStrategy strategy;

    private Student student;
    private Company company;
    private TraineeshipPosition position1;
    private TraineeshipPosition position2;
    private TraineeshipPositionDto positionDto1;
    private TraineeshipPositionDto positionDto2;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setPreferredLocation("New York");
        student.setInterests("java,spring,backend");
        student.setSkills("programming,oop,git");

        company = new Company();
        company.setLocation("New York");

        position1 = new TraineeshipPosition();
        position1.setId(1L);
        position1.setCompany(company);
        position1.setTopics("java,spring");
        position1.setRequiredSkills("programming,oop");

        position2 = new TraineeshipPosition();
        position2.setId(2L);
        position2.setCompany(company);
        position2.setTopics("python,django");
        position2.setRequiredSkills("programming");

        positionDto1 = new TraineeshipPositionDto();
        positionDto1.setId(1L);

        positionDto2 = new TraineeshipPositionDto();
        positionDto2.setId(2L);
    }

    @Test
    void getType_ShouldReturnCombined() {
        assertEquals(RecommendationType.COMBINED, strategy.getType());
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
    void recommendTraineeships_NoLocationMatches_ShouldReturnEmptyList() {
        student.setPreferredLocation("Boston");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1, position2));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_LocationMatchesButLowJaccard_ShouldReturnEmptyList() {
        position1.setTopics("python,machine learning");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1, position2));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_LocationMatchesButMissingSkills_ShouldReturnEmptyList() {
        position1.setRequiredSkills("advanced-math,physics");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(positionRepository.findAvailableStudentPositions()).thenReturn(List.of(position1, position2));

        List<TraineeshipPositionDto> result = strategy.recommendTraineeships(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void recommendTraineeships_CompanyWithoutLocation_ShouldBeFilteredOut() {
        position1.setCompany(null);
        position2.getCompany().setLocation(null);

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
}