package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.springframework.stereotype.Component;

@Component
public class EvaluationMapper {

    public static EvaluationDto toDto(Evaluation entity) {
        if (entity == null) {
            return null;
        }
        EvaluationDto dto = new EvaluationDto();
        dto.setId(entity.getId());
        dto.setTraineeshipPosition(TraineeshipPositionMapper.toDto(entity.getTraineeshipPosition()));
        dto.setProfessorMotivationRating(entity.getProfessorMotivationRating());
        dto.setProfessorEffectivenessRating(entity.getProfessorEffectivenessRating());
        dto.setProfessorEfficiencyRating(entity.getProfessorEfficiencyRating());
        dto.setCompanyMotivationRating(entity.getCompanyMotivationRating());
        dto.setCompanyEffectivenessRating(entity.getCompanyEffectivenessRating());
        dto.setCompanyEfficiencyRating(entity.getCompanyEfficiencyRating());
        dto.setFinalMark(entity.getFinalMark());
        return dto;
    }

    public static Evaluation toEntity(EvaluationDto dto) {
        if (dto == null) {
            return null;
        }
        Evaluation entity = new Evaluation();
        entity.setId(dto.getId());
        entity.setTraineeshipPosition(TraineeshipPositionMapper.toEntity(dto.getTraineeshipPosition()));
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