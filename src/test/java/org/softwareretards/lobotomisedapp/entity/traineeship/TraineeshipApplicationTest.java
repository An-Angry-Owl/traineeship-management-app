package org.softwareretards.lobotomisedapp.entity.traineeship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TraineeshipApplicationTest {

    @InjectMocks
    private TraineeshipApplication traineeshipApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Student student = mock(Student.class);
        TraineeshipPosition position = mock(TraineeshipPosition.class);
        traineeshipApplication = new TraineeshipApplication(student, position);
    }

    @Test
    void testConstructor() {
        assertNotNull(traineeshipApplication.getStudent());
        assertNotNull(traineeshipApplication.getPosition());
        assertNotNull(traineeshipApplication.getApplicationDate());
        assertEquals(ApplicationStatus.PENDING, traineeshipApplication.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        traineeshipApplication.setId(1L);
        assertEquals(1L, traineeshipApplication.getId());

        Student newStudent = mock(Student.class);
        traineeshipApplication.setStudent(newStudent);
        assertEquals(newStudent, traineeshipApplication.getStudent());

        TraineeshipPosition newPosition = mock(TraineeshipPosition.class);
        traineeshipApplication.setPosition(newPosition);
        assertEquals(newPosition, traineeshipApplication.getPosition());

        Timestamp newApplicationDate = new Timestamp(System.currentTimeMillis());
        traineeshipApplication.setApplicationDate(newApplicationDate);
        assertEquals(newApplicationDate, traineeshipApplication.getApplicationDate());

        traineeshipApplication.setStatus(ApplicationStatus.ACCEPTED);
        assertEquals(ApplicationStatus.ACCEPTED, traineeshipApplication.getStatus());
    }

    @Test
    void testDefaultValues() {
        // Test that the default values are correctly set
        assertEquals(ApplicationStatus.PENDING, traineeshipApplication.getStatus());
    }

    @Test
    void testToString() {
        traineeshipApplication.setId(1L);
        traineeshipApplication.setApplicationDate(new Timestamp(1672531200000L));

        String expectedToString = "TraineeshipApplication{" +
                "id=1" +
                ", student=" + traineeshipApplication.getStudent() +
                ", position=" + traineeshipApplication.getPosition() +
                ", applicationDate=" + traineeshipApplication.getApplicationDate() +
                ", status=PENDING" +
                '}';

        assertEquals(expectedToString, traineeshipApplication.toString());
    }
}
