package org.softwareretards.lobotomisedapp.dto;

import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

@Data
public class EvaluationDto {
    private Long id;
    private TraineeshipPositionDto traineeshipPosition;
    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyMotivationRating;
    private Integer companyEffectivenessRating;
    private Integer companyEfficiencyRating;
    private FinalMark finalMark;
}
