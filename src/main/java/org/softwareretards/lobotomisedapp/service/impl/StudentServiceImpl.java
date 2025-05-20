package org.softwareretards.lobotomisedapp.service.impl;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.mapper.LogbookEntryMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipApplicationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.repository.LogbookEntryRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

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
    public StudentDto saveProfile(String username, StudentDto studentDto) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update only profile fields
        student.setFullName(studentDto.getFullName());
        student.setUniversityId(studentDto.getUniversityId());
        student.setInterests(studentDto.getInterests());
        student.setSkills(studentDto.getSkills());
        student.setPreferredLocation(studentDto.getPreferredLocation());

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
    public TraineeshipApplicationDto applyForTraineeship(String studentUsername,
                                                         Long positionId) {
        // Retrieve student
        User user = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }
        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check for existing application
        boolean alreadyApplied = traineeshipApplicationRepository.existsByStudentId(student.getId());

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied");
        }

        // Retrieve position
        TraineeshipPosition position = traineeshipPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));

        // Create and save application
        TraineeshipApplication application = new TraineeshipApplication(student);
        TraineeshipApplication savedApplication = traineeshipApplicationRepository.save(application);

        return TraineeshipApplicationMapper.toDto(savedApplication);
    }


    // TODO: Na aksiopiiso tis imerominies. Feature gia na boro na kano edit palia logbook h kai na vlepo palia (read-only h kati)
    @Override
    @Transactional
    public LogbookEntryDto saveLogbookEntry(String studentUsername, Long positionId, String content) {
        // Validate content
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Logbook entry content cannot be empty");
        }
        // Validate position ID
        if (positionId == null) {
            throw new RuntimeException("Traineeship position ID cannot be null");
        }
        // Validate student username
        if (studentUsername == null || studentUsername.isEmpty()) {
            throw new RuntimeException("Student username cannot be empty");
        }

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
        // Instead of using the mapper to create the entity:
        LogbookEntry entry = new LogbookEntry();
        entry.setStudent(student); // student entity fetched from repository
        entry.setPosition(position); // position entity fetched from repository
        entry.setContent(content);
        entry.setEntryDate(new Timestamp(System.currentTimeMillis()));

        LogbookEntry saved = logbookEntryRepository.save(entry);

        // For the return DTO, use minimal mapping
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(saved.getId());
        dto.setContent(saved.getContent());
        dto.setEntryDate(saved.getEntryDate());
        // Optionally set minimal student/position info if needed
        return dto;

    }

    @Override
    public List<TraineeshipPositionDto> getOpenTraineeshipPositions() {
        List<TraineeshipPosition> positions = traineeshipPositionRepository.findOpenPositions();
        return TraineeshipPositionMapper.toDtoList(positions);
    }

    @Override
    public TraineeshipPositionDto getCurrentTraineeship(String username) {
        List<TraineeshipPosition> positions = traineeshipPositionRepository.findByStudentUsername(username);
        if (positions.isEmpty()) {
            return null;
        }

        return TraineeshipPositionMapper.toDto(positions.get(0));
    }

    @Override
    public List<LogbookEntryDto> getLogbookEntries(String studentUsername, Long positionId) {
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

        // Check if the student is assigned to this position
        if (!position.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Student is not assigned to this position");
        }

        // Retrieve logbook entries
        List<LogbookEntry> entries = logbookEntryRepository.findByStudentAndPosition(student, position);
        return LogbookEntryMapper.toDtoList(entries);
    }

}