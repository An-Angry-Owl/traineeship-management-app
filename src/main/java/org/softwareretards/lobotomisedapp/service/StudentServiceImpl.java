package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.mapper.LogbookEntryMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipApplicationMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.repository.LogbookEntryRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    // Too much duplicate code, but i dont care tbh
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final TraineeshipApplicationRepository traineeshipApplicationRepository;
    private final LogbookEntryRepository logbookEntryRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              TraineeshipPositionRepository traineeshipPositionRepository,
                              TraineeshipApplicationRepository traineeshipApplicationRepository,
                              LogbookEntryRepository logbookEntryRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.traineeshipApplicationRepository = traineeshipApplicationRepository;
        this.logbookEntryRepository = logbookEntryRepository;
    }

    @Override
    @Transactional
    public StudentDto saveProfile(StudentDto studentDto) {
        Student student = StudentMapper.toEntity(studentDto);
        Student savedStudent = studentRepository.save(student);
        return StudentMapper.toDto(savedStudent);
    }

    @Override
    public StudentDto retrieveProfile(String username) {
        // Retrieve student
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return StudentMapper.toDto(student);
    }

    @Override
    @Transactional
    public TraineeshipApplicationDto applyForTraineeship(String studentUsername, Long positionId) {
        // Retrieve student
        User user = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }
        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Retrieve position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        // Create and save application
        TraineeshipApplication application = new TraineeshipApplication(student, position);
        TraineeshipApplication savedApplication = traineeshipApplicationRepository.save(application);

        return TraineeshipApplicationMapper.toDto(savedApplication);
    }


    // TODO: Na aksiopiiso tis imerominies. Feature gia na boro na kano edit palia logbook h kai na vlepo palia (read-only h kati)
    @Override
    @Transactional
    public LogbookEntryDto saveLogbookEntry(String studentUsername, Long positionId, String content) {
        // Retrieve student
        User user = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }
        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Retrieve position and check if assigned to student
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));
        if (position.getStudent() == null || !position.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Student is not assigned to this position");
        }

        // Create and save logbook entry
        LogbookEntry logbookEntry = new LogbookEntry(student, position, content);
        LogbookEntry savedEntry = logbookEntryRepository.save(logbookEntry);

        return LogbookEntryMapper.toDto(savedEntry);
    }
}