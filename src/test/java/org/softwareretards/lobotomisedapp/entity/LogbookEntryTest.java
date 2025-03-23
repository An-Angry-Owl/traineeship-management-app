package org.softwareretards.lobotomisedapp.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LogbookEntryTest {

    @InjectMocks
    private LogbookEntry logbookEntry;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Student student = mock(Student.class);
        TraineeshipPosition position = mock(TraineeshipPosition.class);
        logbookEntry = new LogbookEntry(student, position, "Today I worked on the project.");
    }

    @AfterEach
    void tearDown() {
        logbookEntry = null;
    }

    @Test
    void testConstructor() {
        assertNotNull(logbookEntry.getStudent());
        assertNotNull(logbookEntry.getPosition());
        assertNotNull(logbookEntry.getEntryDate());
        assertEquals("Today I worked on the project.", logbookEntry.getContent());
    }

    @Test
    void testGettersAndSetters() {
        logbookEntry.setId(1L);
        assertEquals(1L, logbookEntry.getId());

        Student newStudent = mock(Student.class);
        logbookEntry.setStudent(newStudent);
        assertEquals(newStudent, logbookEntry.getStudent());

        TraineeshipPosition newPosition = mock(TraineeshipPosition.class);
        logbookEntry.setPosition(newPosition);
        assertEquals(newPosition, logbookEntry.getPosition());

        Timestamp newEntryDate = new Timestamp(System.currentTimeMillis());
        logbookEntry.setEntryDate(newEntryDate);
        assertEquals(newEntryDate, logbookEntry.getEntryDate());

        logbookEntry.setContent("Updated content for the logbook entry.");
        assertEquals("Updated content for the logbook entry.", logbookEntry.getContent());
    }

    @Test
    void testValidationAnnotations() {
        logbookEntry.setContent("a".repeat(65536));

        Set<ConstraintViolation<LogbookEntry>> violations = validator.validate(logbookEntry);
        assertEquals(1, violations.size());
    }

    @Test
    void testToString() {
        logbookEntry.setId(1L);
        logbookEntry.setEntryDate(new Timestamp(1672531200000L));

        String expectedToString = "LogbookEntry{" +
                "id=1" +
                ", student=" + logbookEntry.getStudent() +
                ", position=" + logbookEntry.getPosition() +
                ", entryDate=" + logbookEntry.getEntryDate() +
                ", content='Today I worked on the project.'" +
                '}';

        assertEquals(expectedToString, logbookEntry.toString());
    }
}
