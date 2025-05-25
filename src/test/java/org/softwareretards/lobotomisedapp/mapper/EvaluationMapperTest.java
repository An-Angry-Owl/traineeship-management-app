package org.softwareretards.lobotomisedapp.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(EvaluationMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(EvaluationMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldHandleNullTraineeshipPosition() {
        Evaluation entity = new Evaluation();
        entity.setTraineeshipPosition(null);
        EvaluationDto dto = EvaluationMapper.toDto(entity);
        assertNull(dto.getTraineeshipPositionId());
    }

    @Test
    void toEntity_ShouldHandleNullTraineeshipPosition() {
        EvaluationDto dto = new EvaluationDto();
        dto.setTraineeshipPositionId(null);
        Evaluation entity = EvaluationMapper.toEntity(dto);
        assertNull(entity.getTraineeshipPosition());
    }


    @ParameterizedTest
    @NullSource
    void toDto_ShouldHandleNullRatings(Integer rating) {
        Evaluation entity = new Evaluation();
        entity.setProfStdMotivationRating(rating);
        entity.setProfStdEffectivenessRating(rating);
        entity.setProfStdEfficiencyRating(rating);
        entity.setCompStdMotivationRating(rating);
        entity.setCompStdEffectivenessRating(rating);
        entity.setCompStdEfficiencyRating(rating);

        EvaluationDto dto = EvaluationMapper.toDto(entity);

        assertNull(dto.getProfessorMotivationRating());
        assertNull(dto.getProfessorEffectivenessRating());
        assertNull(dto.getProfessorEfficiencyRating());
        assertNull(dto.getCompanyMotivationRating());
        assertNull(dto.getCompanyEffectivenessRating());
        assertNull(dto.getCompanyEfficiencyRating());
    }
}
