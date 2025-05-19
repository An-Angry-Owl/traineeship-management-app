package org.softwareretards.lobotomisedapp.dto;

import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

@Data
public class EvaluationDto {
    private Long id;
    private Long traineeshipPositionId;
    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyMotivationRating;
    private Integer companyEffectivenessRating;
    private Integer companyEfficiencyRating;

    private Integer compStdMotivationRating;
    private Integer compStdEffectivenessRating;
    private Integer compStdEfficiencyRating;

    private FinalMark finalMark;
}
