package org.softwareretards.lobotomisedapp.service;

import jakarta.transaction.Transactional;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

import java.util.List;
import java.util.Map;

public interface CommitteeService {

    List<CommitteeDto> getAllCommittees();

    CommitteeDto getCommitteeById(Long id);

    CommitteeDto getCommitteeByUsername(String username);

    CommitteeDto createCommittee(CommitteeDto committeeDto);

    CommitteeDto updateCommittee(Long id, CommitteeDto committeeDto);

    CommitteeDto updateCommitteeProfile(String username, CommitteeDto committeeDto);

    void deleteCommittee(Long id);

    List<CommitteeDto> getCommitteesByCommitteeName(String committeeName);

    void assignTraineeshipToStudent(Long committeeId, Long studentId, Long traineeshipId);

    void assignSupervisingProfessor(Long committeeId, Long traineeshipId, Long professorId);

    void monitorTraineeshipEvaluations(Long committeeId, Long traineeshipId);

    void acceptApplication(Long studentId);

    List<TraineeshipPositionDto> getAllTraineeshipPositions();

    List<TraineeshipPositionDto> getAssignedTraineeshipPositions();

    List<StudentDto> getStudentsWithApplications();

    StudentDto getStudentById(Long studentId);

    TraineeshipPositionDto getTraineeshipPositionById(Long positionId);

    Map<Long, Integer> getProfessorWorkloads(List<ProfessorDto> professors);

    EvaluationDto getEvaluationByPositionId(Long positionId);

    @Transactional
    void updateFinalMark(Long positionId, FinalMark finalMark);
}