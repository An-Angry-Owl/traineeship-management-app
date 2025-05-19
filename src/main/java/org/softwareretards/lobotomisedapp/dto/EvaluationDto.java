package org.softwareretards.lobotomisedapp.dto;

import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

@Data
public class EvaluationDto {
    private Long id;
    private Long traineeshipPositionId;
    // Professor-specific ratings
    private Integer profStdMotivationRating;
    private Integer profStdEffectivenessRating;
    private Integer profStdEfficiencyRating;
    private Integer profCompFacilitiesRating;
    private Integer profCompGuidanceRating;

    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyMotivationRating;
    private Integer companyEffectivenessRating;
    private Integer companyEfficiencyRating;
    // Company-specific ratings
    private Integer compStdMotivationRating;
    private Integer compStdEffectivenessRating;
    private Integer compStdEfficiencyRating;

    private FinalMark finalMark;
}
