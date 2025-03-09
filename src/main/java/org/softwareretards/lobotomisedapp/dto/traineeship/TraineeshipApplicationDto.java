package org.softwareretards.lobotomisedapp.dto.traineeship;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.sql.Timestamp;

@Data
public class TraineeshipApplicationDto {
    private Long id;
    private Student student;
    private TraineeshipPositionDto position;
    private Timestamp applicationDate;
    private ApplicationStatus status;
}
