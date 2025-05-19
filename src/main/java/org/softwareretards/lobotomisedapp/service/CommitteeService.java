package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;

import java.util.List;

public interface CommitteeService {

    List<CommitteeDto> getAllCommittees();

    CommitteeDto getCommitteeById(Long id);

    CommitteeDto createCommittee(CommitteeDto committeeDto);

    CommitteeDto updateCommittee(Long id, CommitteeDto committeeDto);

    void deleteCommittee(Long id);

    List<CommitteeDto> getCommitteesByCommitteeName(String committeeName);

    void assignTraineeshipToStudent(Long committeeId, Long studentId, Long traineeshipId);

    void assignSupervisingProfessor(Long committeeId, Long traineeshipId, Long professorId);

    void monitorTraineeshipEvaluations(Long committeeId, Long traineeshipId);

    void acceptApplication(Long studentId);
}