package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionSummaryDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.springframework.stereotype.Component;

@Component
public class EvaluationMapper {

    public EvaluationDto toDto(Evaluation entity) {
        if (entity == null) {
            return null;
        }
        EvaluationDto dto = new EvaluationDto();
        dto.setId(entity.getId());

        // Map to Summary DTO to break circular dependency
        if (entity.getTraineeshipPosition() != null) {
            TraineeshipPositionSummaryDto posDto = new TraineeshipPositionSummaryDto();
            posDto.setId(entity.getTraineeshipPosition().getId());
            posDto.setDescription(entity.getTraineeshipPosition().getDescription());
            posDto.setStatus(entity.getTraineeshipPosition().getStatus());
            dto.setTraineeshipPosition(posDto);
        }

        dto.setProfessorMotivationRating(entity.getProfessorMotivationRating());
        dto.setProfessorEffectivenessRating(entity.getProfessorEffectivenessRating());
        dto.setProfessorEfficiencyRating(entity.getProfessorEfficiencyRating());
        dto.setCompanyMotivationRating(entity.getCompanyMotivationRating());
        dto.setCompanyEffectivenessRating(entity.getCompanyEffectivenessRating());
        dto.setCompanyEfficiencyRating(entity.getCompanyEfficiencyRating());
        dto.setFinalMark(entity.getFinalMark());

        return dto;
    }

    public Evaluation toEntity(EvaluationDto dto) {
        if (dto == null) {
            return null;
        }
        Evaluation entity = new Evaluation();
        entity.setId(dto.getId());

        // Do NOT map full position entity (to avoid circular reference)
        entity.setProfessorMotivationRating(dto.getProfessorMotivationRating());
        entity.setProfessorEffectivenessRating(dto.getProfessorEffectivenessRating());
        entity.setProfessorEfficiencyRating(dto.getProfessorEfficiencyRating());
        entity.setCompanyMotivationRating(dto.getCompanyMotivationRating());
        entity.setCompanyEffectivenessRating(dto.getCompanyEffectivenessRating());
        entity.setCompanyEfficiencyRating(dto.getCompanyEfficiencyRating());
        entity.setFinalMark(dto.getFinalMark());

        return entity;
    }
}
