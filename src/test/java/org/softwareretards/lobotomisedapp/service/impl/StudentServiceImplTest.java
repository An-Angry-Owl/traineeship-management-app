package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.repository.LogbookEntryRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TraineeshipPositionRepository traineeshipPositionRepository;

    @Mock
    private TraineeshipApplicationRepository traineeshipApplicationRepository;

    @Mock
    private LogbookEntryRepository logbookEntryRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void saveProfileShouldSaveAndReturnStudentDto() {
        Student student = new Student();
        student.setUsername("studentUser");

        StudentDto studentDto = new StudentDto();
        studentDto.setFullName("Test Student");

        when(studentRepository.findByUsername("studentUser")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto result = studentService.saveProfile("studentUser", studentDto);

        assertEquals("Test Student", result.getFullName());
    }

    @Test
    void retrieveProfileShouldReturnStudentDtoWhenUserIsStudent() {
        User user = new User();
        user.setId(1L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setId(1L);
        student.setUsername("studentUser");

        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDto result = studentService.retrieveProfile("studentUser");

        assertEquals("studentUser", result.getUserDto().getUsername());
    }

    @Test
    void retrieveProfileShouldThrowExceptionWhenUserIsNotStudent() {
        User user = new User();
        user.setId(1L);
        user.setUsername("nonStudentUser");
        user.setRole(Role.PROFESSOR);

        when(userRepository.findByUsername("nonStudentUser")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> studentService.retrieveProfile("nonStudentUser"));
    }

    @Test
    void saveLogbookEntryShouldSaveAndReturnLogbookEntryDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setId(1L);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(2L);
        position.setStudent(student);

        LogbookEntry logbookEntry = new LogbookEntry(student, position, "Log content");

        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(position));
        when(logbookEntryRepository.save(any(LogbookEntry.class))).thenReturn(logbookEntry);

        LogbookEntryDto result = studentService.saveLogbookEntry("studentUser", 2L, "Log content");

        assertEquals("Log content", result.getContent());
    }

    @Test
    void getLogbookEntriesShouldReturnEntriesWhenStudentAssignedToPosition() {
        User user = new User();
        user.setId(1L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setId(1L);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(2L);
        position.setStudent(student);

        LogbookEntry entry = new LogbookEntry(student, position, "Log content");
        entry.setId(1L);

        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(position));
        when(logbookEntryRepository.findByStudentAndPosition(student, position)).thenReturn(List.of(entry));

        List<LogbookEntryDto> result = studentService.getLogbookEntries("studentUser", 2L);

        assertEquals(1, result.size());
        assertEquals("Log content", result.get(0).getContent());
    }

    @Test
    void getOpenTraineeshipPositionsShouldReturnListOfOpenPositions() {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);

        when(traineeshipPositionRepository.findOpenPositions()).thenReturn(List.of(position));

        List<TraineeshipPositionDto> result = studentService.getOpenTraineeshipPositions();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getCurrentTraineeshipShouldReturnCurrentPosition() {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);

        when(traineeshipPositionRepository.findByStudentUsername("studentUser")).thenReturn(List.of(position));

        TraineeshipPositionDto result = studentService.getCurrentTraineeship("studentUser");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void saveProfileShouldThrowExceptionWhenStudentNotFound() {
        when(studentRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());
        StudentDto studentDto = new StudentDto();
        assertThrows(RuntimeException.class, () -> studentService.saveProfile("unknownUser", studentDto));
    }

    @Test
    void retrieveProfileShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.retrieveProfile("missingUser"));
    }

    @Test
    void retrieveProfileShouldThrowExceptionWhenStudentProfileNotFound() {
        User user = new User();
        user.setId(2L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.retrieveProfile("studentUser"));
    }

    @Test
    void applyForTraineeshipShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.applyForTraineeship("missingUser", 1L));
    }

    @Test
    void applyForTraineeshipShouldThrowExceptionWhenUserIsNotStudent() {
        User user = new User();
        user.setId(3L);
        user.setUsername("notStudent");
        user.setRole(Role.PROFESSOR);
        when(userRepository.findByUsername("notStudent")).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> studentService.applyForTraineeship("notStudent", 1L));
    }

    @Test
    void applyForTraineeshipShouldThrowExceptionWhenStudentNotFound() {
        User user = new User();
        user.setId(4L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.applyForTraineeship("studentUser", 1L));
    }

    @Test
    void applyForTraineeshipShouldThrowExceptionWhenAlreadyApplied() {
        User user = new User();
        user.setId(5L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(5L);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(traineeshipApplicationRepository.existsByStudentId(5L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> studentService.applyForTraineeship("studentUser", 1L));
    }

    @Test
    void applyForTraineeshipShouldThrowExceptionWhenPositionNotFound() {
        User user = new User();
        user.setId(6L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(6L);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(6L)).thenReturn(Optional.of(student));
        when(traineeshipApplicationRepository.existsByStudentId(6L)).thenReturn(false);
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.applyForTraineeship("studentUser", 1L));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenContentIsEmpty() {
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, ""));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenPositionIdIsNull() {
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", null, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenUsernameIsEmpty() {
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenUserIsNotStudent() {
        User user = new User();
        user.setId(7L);
        user.setUsername("studentUser");
        user.setRole(Role.PROFESSOR);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenStudentNotFound() {
        User user = new User();
        user.setId(8L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenPositionNotFound() {
        User user = new User();
        user.setId(9L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(9L);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(9L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenStudentNotAssignedToPosition() {
        User user = new User();
        user.setId(10L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(10L);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);
        position.setStudent(null);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void saveLogbookEntryShouldThrowExceptionWhenStudentIdDoesNotMatchPositionStudent() {
        User user = new User();
        user.setId(11L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(11L);
        Student otherStudent = new Student();
        otherStudent.setId(12L);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);
        position.setStudent(otherStudent);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(11L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));
        assertThrows(RuntimeException.class, () -> studentService.saveLogbookEntry("studentUser", 1L, "content"));
    }

    @Test
    void getLogbookEntriesShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.getLogbookEntries("studentUser", 1L));
    }

    @Test
    void getLogbookEntriesShouldThrowExceptionWhenUserIsNotStudent() {
        User user = new User();
        user.setId(13L);
        user.setUsername("studentUser");
        user.setRole(Role.PROFESSOR);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> studentService.getLogbookEntries("studentUser", 1L));
    }

    @Test
    void getLogbookEntriesShouldThrowExceptionWhenStudentNotFound() {
        User user = new User();
        user.setId(14L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(14L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.getLogbookEntries("studentUser", 1L));
    }

    @Test
    void getLogbookEntriesShouldThrowExceptionWhenPositionNotFound() {
        User user = new User();
        user.setId(15L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(15L);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(15L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> studentService.getLogbookEntries("studentUser", 1L));
    }

    @Test
    void getLogbookEntriesShouldThrowExceptionWhenStudentNotAssignedToPosition() {
        User user = new User();
        user.setId(16L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);
        Student student = new Student();
        student.setId(16L);
        Student otherStudent = new Student();
        otherStudent.setId(17L);
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1L);
        position.setStudent(otherStudent);
        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(16L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(1L)).thenReturn(Optional.of(position));
        assertThrows(RuntimeException.class, () -> studentService.getLogbookEntries("studentUser", 1L));
    }

    @Test
    void getCurrentTraineeshipShouldReturnNullWhenNoPositionsFound() {
        when(traineeshipPositionRepository.findByStudentUsername("studentUser")).thenReturn(List.of());
        TraineeshipPositionDto result = studentService.getCurrentTraineeship("studentUser");
        assertNull(result);
    }

}
