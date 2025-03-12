package org.softwareretards.lobotomisedapp.dto.traineeship;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;

@Data
public class TraineeshipPositionSummaryDto {
    private Long id;
    private String description;
    private TraineeshipStatus status;
}
