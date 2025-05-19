package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.springframework.stereotype.Component;

@Component
public class EvaluationMapper {

    public static EvaluationDto toDto(Evaluation entity) {
        if (entity == null) return null;

        EvaluationDto dto = new EvaluationDto();
        dto.setId(entity.getId());
        if (entity.getTraineeshipPosition() != null) {
            dto.setTraineeshipPositionId(entity.getTraineeshipPosition().getId());
        }
        dto.setProfStdMotivationRating(entity.getProfStdMotivationRating());
        dto.setProfStdEffectivenessRating(entity.getProfStdEffectivenessRating());
        dto.setProfStdEfficiencyRating(entity.getProfStdEfficiencyRating());
        dto.setProfCompFacilitiesRating(entity.getProfCompFacilitiesRating());
        dto.setProfCompGuidanceRating(entity.getProfCompGuidanceRating());

        dto.setCompStdMotivationRating(entity.getCompStdMotivationRating());
        dto.setCompStdEffectivenessRating(entity.getCompStdEffectivenessRating());
        dto.setCompStdEfficiencyRating(entity.getCompStdEfficiencyRating());

        dto.setFinalMark(entity.getFinalMark());

        return dto;
    }

    public static Evaluation toEntity(EvaluationDto dto) {
        if (dto == null) return null;

        Evaluation entity = new Evaluation();
        entity.setId(dto.getId());
        entity.setProfStdMotivationRating(dto.getProfStdMotivationRating());
        entity.setProfStdEffectivenessRating(dto.getProfStdEffectivenessRating());
        entity.setProfStdEfficiencyRating(dto.getProfStdEfficiencyRating());
        entity.setProfCompFacilitiesRating(dto.getProfCompFacilitiesRating());
        entity.setProfCompGuidanceRating(dto.getProfCompGuidanceRating());

        entity.setCompStdMotivationRating(dto.getCompStdMotivationRating());
        entity.setCompStdEffectivenessRating(dto.getCompStdEffectivenessRating());
        entity.setCompStdEfficiencyRating(dto.getCompStdEfficiencyRating());

        entity.setFinalMark(dto.getFinalMark());

        return entity;
    }
}