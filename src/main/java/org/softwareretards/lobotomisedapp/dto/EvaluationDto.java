package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

@Data
public class EvaluationDto {
    private int id;
    private TraineeshipPosition traineeshipPosition;
    private Integer professorMotivationRating;
    private Integer professorEffectivenessRating;
    private Integer professorEfficiencyRating;
    private Integer companyMotivationRating;
    private Integer companyEffectivenessRating;
    private Integer companyEfficiencyRating;
    private FinalMark finalMark;
}
