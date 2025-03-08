package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.userDtos.StudentDto;
import org.softwareretards.lobotomisedapp.entity.TraineeshipPosition;

import java.sql.Timestamp;

@Data
public class LogbookDto {
    private int id;
    private StudentDto student;
    private TraineeshipPosition traineeship;
    private Timestamp timestamp;
    private String content;
}
