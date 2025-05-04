package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
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

    //@Test
    //void saveProfileShouldSaveAndReturnStudentDto() {
    //    UserDto userDto = new UserDto();
    //    userDto.setUsername("newStudent");
//
    //    StudentDto studentDto = new StudentDto();
    //    studentDto.setUserDto(userDto);
//
    //    Student student = new Student();
    //    student.setUsername("studentUser");
//
    //    when(studentRepository.save(any(Student.class))).thenReturn(student);
//
    //    StudentDto result = studentService.saveProfile(studentDto);
//
    //    assertEquals("studentUser", result.getUserDto().getUsername());
    //}

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
    void applyForTraineeshipShouldSaveAndReturnApplicationDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("studentUser");
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setId(1L);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(2L);

        TraineeshipApplication application = new TraineeshipApplication(student, position);

        when(userRepository.findByUsername("studentUser")).thenReturn(Optional.of(user));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(traineeshipPositionRepository.findById(2L)).thenReturn(Optional.of(position));
        when(traineeshipApplicationRepository.save(any(TraineeshipApplication.class))).thenReturn(application);

        TraineeshipApplicationDto result = studentService.applyForTraineeship("studentUser", 2L);

        assertEquals(2L, result.getPosition().getId());
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
}
