package org.softwareretards.lobotomisedapp.entity.traineeship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = WavefrontProperties.Application.class)
class TraineeshipPositionTest {

    @InjectMocks
    private TraineeshipPosition traineeshipPosition;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        traineeshipPosition = new TraineeshipPosition();
    }

    @AfterEach
    void tearDown() {
        traineeshipPosition = null;
    }

    @Test
    void testDefaultConstructor() {
        assertNull(traineeshipPosition.getId());
        assertNull(traineeshipPosition.getCompany());
        assertNull(traineeshipPosition.getStudent());
        assertNull(traineeshipPosition.getProfessor());
        assertNull(traineeshipPosition.getStartDate());
        assertNull(traineeshipPosition.getEndDate());
        assertNull(traineeshipPosition.getDescription());
        assertNull(traineeshipPosition.getRequiredSkills());
        assertNull(traineeshipPosition.getTopics());
        assertEquals(TraineeshipStatus.OPEN, traineeshipPosition.getStatus());
        assertNotNull(traineeshipPosition.getLogbookEntries());
        assertTrue(traineeshipPosition.getLogbookEntries().isEmpty());
        assertNull(traineeshipPosition.getEvaluation());
    }

    @Test
    void testGettersAndSetters() {
        traineeshipPosition.setId(1L);
        assertEquals(1L, traineeshipPosition.getId());

        Company company = mock(Company.class);
        traineeshipPosition.setCompany(company);
        assertEquals(company, traineeshipPosition.getCompany());

        Student student = mock(Student.class);
        traineeshipPosition.setStudent(student);
        assertEquals(student, traineeshipPosition.getStudent());

        Professor professor = mock(Professor.class);
        traineeshipPosition.setProfessor(professor);
        assertEquals(professor, traineeshipPosition.getProfessor());

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        traineeshipPosition.setStartDate(startDate);
        assertEquals(startDate, traineeshipPosition.getStartDate());

        LocalDate endDate = LocalDate.of(2023, 6, 30);
        traineeshipPosition.setEndDate(endDate);
        assertEquals(endDate, traineeshipPosition.getEndDate());

        traineeshipPosition.setDescription("Software Development Internship");
        assertEquals("Software Development Internship", traineeshipPosition.getDescription());

        traineeshipPosition.setRequiredSkills("Java, Spring Boot, SQL");
        assertEquals("Java, Spring Boot, SQL", traineeshipPosition.getRequiredSkills());

        traineeshipPosition.setTopics("Web Development, Database Management");
        assertEquals("Web Development, Database Management", traineeshipPosition.getTopics());

        traineeshipPosition.setStatus(TraineeshipStatus.ASSIGNED);
        assertEquals(TraineeshipStatus.ASSIGNED, traineeshipPosition.getStatus());

        List<LogbookEntry> logbookEntries = new ArrayList<>();
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        logbookEntries.add(logbookEntry);
        traineeshipPosition.setLogbookEntries(logbookEntries);
        assertEquals(logbookEntries, traineeshipPosition.getLogbookEntries());

        Evaluation evaluation = mock(Evaluation.class);
        traineeshipPosition.setEvaluation(evaluation);
        assertEquals(evaluation, traineeshipPosition.getEvaluation());
    }

    @Test
    void testToString() {
        traineeshipPosition.setId(1L);
        traineeshipPosition.setStartDate(LocalDate.of(2023, 1, 1));
        traineeshipPosition.setEndDate(LocalDate.of(2023, 6, 30));
        traineeshipPosition.setDescription("Software Development Internship");
        traineeshipPosition.setRequiredSkills("Java, Spring Boot, SQL");
        traineeshipPosition.setTopics("Web Development, Database Management");
        traineeshipPosition.setStatus(TraineeshipStatus.ASSIGNED);

        String expectedToString = "TraineeshipPosition{" +
                "id=1" +
                ", startDate=2023-01-01" +
                ", endDate=2023-06-30" +
                ", description='Software Development Internship'" +
                ", requiredSkills='Java, Spring Boot, SQL'" +
                ", topics='Web Development, Database Management'" +
                ", status=ASSIGNED" +
                '}';

        assertEquals(expectedToString, traineeshipPosition.toString());
    }

    @Test
    void testLogbookEntriesAssociation() {
        LogbookEntry logbookEntry = mock(LogbookEntry.class);
        traineeshipPosition.getLogbookEntries().add(logbookEntry);

        assertEquals(1, traineeshipPosition.getLogbookEntries().size());
        assertTrue(traineeshipPosition.getLogbookEntries().contains(logbookEntry));
    }

    @Test
    void testEvaluationAssociation() {
        Evaluation evaluation = mock(Evaluation.class);
        traineeshipPosition.setEvaluation(evaluation);

        assertEquals(evaluation, traineeshipPosition.getEvaluation());
    }
}
