package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
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

    public List<CommitteeDto> getAllCommittees() {
        return committeeRepository.findAll().stream()
                .map(CommitteeMapper::toDto)
                .toList();
    }

    public CommitteeDto getCommitteeById(Long id) {
        Optional<Committee> committee = committeeRepository.findById(id);
        return committee.map(CommitteeMapper::toDto).orElse(null);
    }

    public CommitteeDto createCommittee(CommitteeDto committeeDto) {
        Committee committee = CommitteeMapper.toEntity(committeeDto);
        Committee savedCommittee = committeeRepository.save(committee);
        return CommitteeMapper.toDto(savedCommittee);
    }

    public CommitteeDto updateCommittee(Long id, CommitteeDto committeeDto) {
        Optional<Committee> existingCommitteeOpt = committeeRepository.findById(id);
        if (existingCommitteeOpt.isPresent()) {
            Committee existingCommittee = existingCommitteeOpt.get();
            existingCommittee.setUsername(committeeDto.getUserDto().getUsername());
            existingCommittee.setCommitteeName(committeeDto.getCommitteeName());
            Committee updatedCommittee = committeeRepository.save(existingCommittee);
            return CommitteeMapper.toDto(updatedCommittee);
        }
        return null;
    }

    public void deleteCommittee(Long id) {
        committeeRepository.deleteById(id);
    }

    public List<CommitteeDto> getCommitteesByCommitteeName(String committeeName) {
        return committeeRepository.findByCommitteeName(committeeName).stream()
                .map(CommitteeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void assignTraineeshipToStudent(Long committeeId, Long studentId, Long traineeshipId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<TraineeshipPosition> traineeshipOpt = traineeshipPositionRepository.findById(traineeshipId);

        if (studentOpt.isPresent() && traineeshipOpt.isPresent()) {
            Student student = studentOpt.get();
            TraineeshipPosition traineeship = traineeshipOpt.get();

            // Assign the student to the traineeship
            traineeship.setStudent(student);
            traineeship.setStatus(TraineeshipStatus.ASSIGNED);
            traineeshipPositionRepository.save(traineeship);
        } else {
            throw new RuntimeException("Student or Traineeship not found");
        }
    }


    @Override
    @Transactional
    public void assignSupervisingProfessor(Long committeeId, Long traineeshipId, Long professorId) {
        // Fetch the traineeship and professor entities
        Optional<TraineeshipPosition> traineeshipOpt = traineeshipPositionRepository.findById(traineeshipId);
        Optional<Professor> professorOpt = professorRepository.findById(professorId);

        if (traineeshipOpt.isPresent() && professorOpt.isPresent()) {
            TraineeshipPosition traineeship = traineeshipOpt.get();
            Professor professor = professorOpt.get();

            // Assign the professor to the traineeship
            traineeship.setProfessor(professor);
            traineeshipPositionRepository.save(traineeship);
        } else {
            //TODO: This should be handled better with a custom exception
            throw new RuntimeException("Traineeship or Professor not found");
        }
    }

    @Override
    public void monitorTraineeshipEvaluations(Long committeeId, Long traineeshipId) {
        Optional<TraineeshipPosition> traineeshipOpt = traineeshipPositionRepository.findById(traineeshipId);
        Optional<Evaluation> evaluationOpt = traineeshipOpt.map(TraineeshipPosition::getEvaluation);

        if (traineeshipOpt.isPresent()) {
            TraineeshipPosition traineeship = traineeshipOpt.get();
            Evaluation evaluation = evaluationOpt.orElse(null);

            if (evaluation != null) {
                //FIXME: This is a security issue, we should not print the final mark
                // I am not going to fix you.
                System.out.println("Traineeship evaluation: " + evaluation.getFinalMark());
            } else {
                System.out.println("No evaluation found for traineeship");
            }

        } else {
            throw new RuntimeException("Traineeship not found");
        }
    }
}
