package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.mapper.user.CommitteeMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.CommitteeRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private final CommitteeRepository committeeRepository;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository committeeRepository,
                                TraineeshipPositionRepository traineeshipPositionRepository,
                                StudentRepository studentRepository,
                                ProfessorRepository professorRepository) {
        this.committeeRepository = committeeRepository;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public List<CommitteeDto> getAllCommittees() {
        return committeeRepository.findAll().stream()
                .map(CommitteeMapper::toDto)
                .toList();
    }

    @Override
    public CommitteeDto getCommitteeById(Long id) {
        return committeeRepository.findById(id)
                .map(CommitteeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Committee not found with ID: " + id));
    }

    @Override
    public CommitteeDto createCommittee(CommitteeDto committeeDto) {
        Committee committee = committeeRepository.findByUsername(committeeDto.getUsername())
                .stream()
                .filter(c -> c.getUsername().equals(committeeDto.getUsername()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Committee already exists with username: " + committeeDto.getUsername()));
        if (committee != null) {
            throw new RuntimeException("Committee already exists with username: " + committeeDto.getUsername());
        } else {
            Committee newCommittee = CommitteeMapper.toEntity(committeeDto);
            newCommittee.setRole(Role.COMMITTEE);
            Committee savedCommittee = committeeRepository.save(newCommittee);
            return CommitteeMapper.toDto(savedCommittee);
        }
    }

    @Override
    public CommitteeDto updateCommittee(Long id, CommitteeDto committeeDto) {
        Committee existingCommittee = committeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Committee not found with ID: " + id));

        existingCommittee.setUsername(committeeDto.getUsername());
        existingCommittee.setCommitteeName(committeeDto.getCommitteeName());
        existingCommittee.setEnabled(committeeDto.isEnabled());
        existingCommittee.setCreatedAt(committeeDto.getCreatedAt());
        existingCommittee.setUpdatedAt(committeeDto.getUpdatedAt());

        Committee updatedCommittee = committeeRepository.save(existingCommittee);
        return CommitteeMapper.toDto(updatedCommittee);
    }

    @Override
    public void deleteCommittee(Long id) {
        committeeRepository.deleteById(id);
    }

    @Override
    public List<CommitteeDto> getCommitteesByCommitteeName(String committeeName) {
        return committeeRepository.findByCommitteeName(committeeName).stream()
                .map(CommitteeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void assignTraineeshipToStudent(Long committeeId, Long studentId, Long traineeshipId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found with ID: " + traineeshipId));

        traineeship.setStudent(student);
        traineeshipPositionRepository.save(traineeship);
    }

    @Override
    @Transactional
    public void assignSupervisingProfessor(Long committeeId, Long traineeshipId, Long professorId) {
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found with ID: " + traineeshipId));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found with ID: " + professorId));

        traineeship.setProfessor(professor);
        traineeshipPositionRepository.save(traineeship);
    }

    @Override
    public void monitorTraineeshipEvaluations(Long committeeId, Long traineeshipId) {
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found with ID: " + traineeshipId));
        Evaluation evaluation = Optional.ofNullable(traineeship.getEvaluation())
                .orElseThrow(() -> new RuntimeException("No evaluation found for traineeship"));

        // Handle evaluation monitoring logic here
        System.out.println("Traineeship evaluation: " + evaluation.getFinalMark());
    }
}