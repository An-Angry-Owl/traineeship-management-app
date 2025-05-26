package org.softwareretards.lobotomisedapp.service.impl;

import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.CommitteeMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.CommitteeRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private final CommitteeRepository committeeRepository;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final TraineeshipApplicationRepository traineeshipApplicationRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository committeeRepository,
                                TraineeshipPositionRepository traineeshipPositionRepository,
                                StudentRepository studentRepository,
                                ProfessorRepository professorRepository,
                                TraineeshipApplicationRepository traineeshipApplicationRepository,
                                UserRepository userRepository,
                                EvaluationRepository evaluationRepository) {
        this.committeeRepository = committeeRepository;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.traineeshipApplicationRepository = traineeshipApplicationRepository;
        this.userRepository = userRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public List<CommitteeDto> getAllCommittees() {
        return committeeRepository.findAll().stream()
                .map(CommitteeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommitteeDto getCommitteeById(Long id) {
        return committeeRepository.findById(id)
                .map(CommitteeMapper::toDto)
                .orElse(null);
    }

    @Override
    public CommitteeDto getCommitteeByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .flatMap(committeeRepository::findById)
                .map(CommitteeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Committee not found"));
    }

    @Override
    public CommitteeDto createCommittee(CommitteeDto committeeDto) {
        Committee committee = CommitteeMapper.toEntity(committeeDto);
        Committee savedCommittee = committeeRepository.save(committee);
        return CommitteeMapper.toDto(savedCommittee);
    }

    @Override
    public CommitteeDto updateCommittee(Long id, CommitteeDto committeeDto) {
        return committeeRepository.findById(id)
                .map(existingCommittee -> {
                    existingCommittee.setUsername(committeeDto.getUserDto().getUsername());
                    existingCommittee.setCommitteeName(committeeDto.getCommitteeName());
                    Committee updatedCommittee = committeeRepository.save(existingCommittee);
                    return CommitteeMapper.toDto(updatedCommittee);
                })
                .orElse(null);
    }

    @Override
    public CommitteeDto updateCommitteeProfile(String username, CommitteeDto committeeDto) {
        CommitteeDto existingProfile = getCommitteeByUsername(username);
        committeeDto.setUserDto(existingProfile.getUserDto());
        return updateCommittee(existingProfile.getUserDto().getId(), committeeDto);
    }

    @Override
    public void deleteCommittee(Long id) {
        committeeRepository.deleteById(id);
    }

    @Override
    public List<CommitteeDto> getCommitteesByCommitteeName(String committeeName) {
        return committeeRepository.findByCommitteeName(committeeName).stream()
                .map(CommitteeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignTraineeshipToStudent(Long committeeId, Long studentId, Long traineeshipId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found"));

        traineeship.setStudent(student);
        traineeship.setStatus(TraineeshipStatus.ASSIGNED);
        traineeshipPositionRepository.save(traineeship);
    }

    @Override
    @Transactional
    public void assignSupervisingProfessor(Long committeeId, Long traineeshipId, Long professorId) {
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found"));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        traineeship.setProfessor(professor);
        traineeship.setStatus(TraineeshipStatus.IN_PROGRESS);
        traineeshipPositionRepository.save(traineeship);
    }

    @Override
    public void monitorTraineeshipEvaluations(Long committeeId, Long traineeshipId) {
        TraineeshipPosition traineeship = traineeshipPositionRepository.findById(traineeshipId)
                .orElseThrow(() -> new RuntimeException("Traineeship not found"));

        Evaluation evaluation = traineeship.getEvaluation();
        if (evaluation != null) {
            System.out.println("Traineeship evaluation: " + evaluation.getFinalMark());
        } else {
            System.out.println("No evaluation found for traineeship");
        }
    }

    @Override
    @Transactional
    public void acceptApplication(Long studentId) {
        TraineeshipApplication application = traineeshipApplicationRepository.findByStudentId(studentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No applications found for student"));

        if (application.getStatus() == ApplicationStatus.ACCEPTED) {
            throw new RuntimeException("Application is already accepted");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        traineeshipApplicationRepository.save(application);
    }

    @Override
    public List<TraineeshipPositionDto> getAllTraineeshipPositions() {
        return traineeshipPositionRepository.findAll().stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TraineeshipPositionDto> getAssignedTraineeshipPositions() {
        return traineeshipPositionRepository.findAssignedPositions().stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getStudentsWithApplications() {
        List<Long> studentIds = traineeshipApplicationRepository.findDistinctStudentIds();
        return studentRepository.findAllById(studentIds).stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(StudentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public TraineeshipPositionDto getTraineeshipPositionById(Long positionId) {
        return traineeshipPositionRepository.findById(positionId)
                .map(TraineeshipPositionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Traineeship position not found"));
    }

    @Override
    public Map<Long, Integer> getProfessorWorkloads(List<ProfessorDto> professors) {
        Map<Long, Integer> workloads = new HashMap<>();
        professors.forEach(prof -> {
            int workload = traineeshipPositionRepository.countPositionsByProfessorId(prof.getUserDto().getId());
            workloads.put(prof.getUserDto().getId(), workload);
        });
        return workloads;
    }

    @Override
    public EvaluationDto getEvaluationByPositionId(Long positionId) {
        return evaluationRepository.findByTraineeshipPositionId(positionId)
                .map(EvaluationMapper::toDto)
                .orElse(null);
    }

    @Transactional
    @Override
    public void updateFinalMark(Long positionId, FinalMark finalMark) {
        Evaluation evaluation = evaluationRepository.findByTraineeshipPositionId(positionId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        evaluation.setFinalMark(finalMark);

        TraineeshipPosition position = evaluation.getTraineeshipPosition();
        if (finalMark.equals(FinalMark.PASS) || finalMark.equals(FinalMark.FAIL)) {
            position.setStatus(TraineeshipStatus.COMPLETED);
        } else if (finalMark.equals(FinalMark.PENDING)) {
            position.setStatus(TraineeshipStatus.IN_PROGRESS);
        } else {
            throw new RuntimeException("Invalid final mark");
        }
        evaluationRepository.save(evaluation);
    }
}
