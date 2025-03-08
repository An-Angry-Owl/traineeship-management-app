package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.TraineeshipPosition;

import java.sql.Timestamp;

@Data
public class LogbookEntryDto {
    private Long id;
    private StudentDto student;
    private TraineeshipPosition position;
    private Timestamp entryDate;
    private String content;
}
