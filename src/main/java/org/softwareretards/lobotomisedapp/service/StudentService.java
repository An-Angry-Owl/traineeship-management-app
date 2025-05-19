package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto saveProfile(String username, StudentDto studentDto);
    StudentDto retrieveProfile(String username);
    TraineeshipApplicationDto applyForTraineeship(String studentUsername, Long positionId);
    LogbookEntryDto saveLogbookEntry(String studentUsername, Long positionId, String content);
    List<TraineeshipPositionDto> getOpenTraineeshipPositions();
    TraineeshipPositionDto getCurrentTraineeship(String username);

    List<LogbookEntryDto> getLogbookEntries(String username, Long id);
}