package org.softwareretards.lobotomisedapp.dto.traineeship;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;

import java.sql.Timestamp;

@Data
public class TraineeshipApplicationDto {
    private Long id;
    private StudentDto student;
    private Timestamp applicationDate;
    private ApplicationStatus status;
}
