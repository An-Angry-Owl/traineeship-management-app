package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;

public interface StudentService {
    StudentDto saveProfile(StudentDto studentDto);
    StudentDto retrieveProfile(String username);
    TraineeshipApplicationDto applyForTraineeship(String studentUsername, Long positionId);
    LogbookEntryDto saveLogbookEntry(String studentUsername, Long positionId, String content);
}