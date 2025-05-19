package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
public class LogbookEntryDto {
    private Long id;
    private StudentDto student;
    private TraineeshipPositionDto position;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp entryDate;
    private String content;
}
