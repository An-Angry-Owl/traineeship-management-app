package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionSummaryDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;

import java.sql.Timestamp;

@Data
public class LogbookEntryDto {
    private Long id;
    private StudentDto student;
    private TraineeshipPositionSummaryDto position;
    private Timestamp entryDate;
    private String content;
}
