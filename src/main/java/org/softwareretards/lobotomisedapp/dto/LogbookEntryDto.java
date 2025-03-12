package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.user.StudentNDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;

import java.sql.Timestamp;

@Data
public class LogbookEntryDto {
    private Long id;
    private StudentNDto student;
    private TraineeshipPosition position;
    private Timestamp entryDate;
    private String content;
}
