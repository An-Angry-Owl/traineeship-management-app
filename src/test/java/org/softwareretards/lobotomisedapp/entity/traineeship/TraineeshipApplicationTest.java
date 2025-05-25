package org.softwareretards.lobotomisedapp.entity.traineeship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeshipApplicationTest {

    @InjectMocks
    private TraineeshipApplication traineeshipApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Student student = mock(Student.class);
        traineeshipApplication = new TraineeshipApplication(student);
    }

    void constructorShouldInitializeFieldsCorrectly() {
        assertNotNull(traineeshipApplication.getStudent());
        assertNotNull(traineeshipApplication.getApplicationDate());
        assertEquals(ApplicationStatus.PENDING, traineeshipApplication.getStatus());
    }

    void defaultValuesShouldBeSetCorrectly() {
        assertEquals(ApplicationStatus.PENDING, traineeshipApplication.getStatus());
    }

    void toStringShouldReturnExpectedFormat() {
        traineeshipApplication.setId(1L);
        traineeshipApplication.setApplicationDate(new Timestamp(1672531200000L));

        String expectedToString = "TraineeshipApplication{" +
                "id=1" +
                ", student=" + traineeshipApplication.getStudent() +
                ", applicationDate=" + traineeshipApplication.getApplicationDate() +
                ", status=PENDING" +
                '}';

        assertEquals(expectedToString, traineeshipApplication.toString());
    }

    void applicationDateShouldBeImmutable() {
        Timestamp initialDate = traineeshipApplication.getApplicationDate();
        Timestamp newDate = new Timestamp(System.currentTimeMillis() + 10000);
        traineeshipApplication.setApplicationDate(newDate);

        assertNotEquals(initialDate, traineeshipApplication.getApplicationDate());
    }

    void statusShouldBeUpdatable() {
        traineeshipApplication.setStatus(ApplicationStatus.REJECTED);
        assertEquals(ApplicationStatus.REJECTED, traineeshipApplication.getStatus());
    }
}