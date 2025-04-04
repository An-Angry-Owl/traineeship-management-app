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
    void toDto_ShouldMapAllFieldsCorrectly() {
        Evaluation entity = new Evaluation();
        entity.setId(1L);
        entity.setProfessorMotivationRating(4);
        entity.setProfessorEffectivenessRating(5);
        entity.setProfessorEfficiencyRating(3);
        entity.setCompanyMotivationRating(2);
        entity.setCompanyEffectivenessRating(4);
        entity.setCompanyEfficiencyRating(5);
        entity.setFinalMark(FinalMark.PASS);

        EvaluationDto dto = EvaluationMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getProfessorMotivationRating(), dto.getProfessorMotivationRating());
        assertEquals(entity.getProfessorEffectivenessRating(), dto.getProfessorEffectivenessRating());
        assertEquals(entity.getProfessorEfficiencyRating(), dto.getProfessorEfficiencyRating());
        assertEquals(entity.getCompanyMotivationRating(), dto.getCompanyMotivationRating());
        assertEquals(entity.getCompanyEffectivenessRating(), dto.getCompanyEffectivenessRating());
        assertEquals(entity.getCompanyEfficiencyRating(), dto.getCompanyEfficiencyRating());
        assertEquals(entity.getFinalMark(), dto.getFinalMark());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        EvaluationDto dto = new EvaluationDto();
        dto.setId(1L);
        dto.setProfessorMotivationRating(4);
        dto.setProfessorEffectivenessRating(5);
        dto.setProfessorEfficiencyRating(3);
        dto.setCompanyMotivationRating(2);
        dto.setCompanyEffectivenessRating(4);
        dto.setCompanyEfficiencyRating(5);
        dto.setFinalMark(FinalMark.PASS);

        Evaluation entity = EvaluationMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getProfessorMotivationRating(), entity.getProfessorMotivationRating());
        assertEquals(dto.getProfessorEffectivenessRating(), entity.getProfessorEffectivenessRating());
        assertEquals(dto.getProfessorEfficiencyRating(), entity.getProfessorEfficiencyRating());
        assertEquals(dto.getCompanyMotivationRating(), entity.getCompanyMotivationRating());
        assertEquals(dto.getCompanyEffectivenessRating(), entity.getCompanyEffectivenessRating());
        assertEquals(dto.getCompanyEfficiencyRating(), entity.getCompanyEfficiencyRating());
        assertEquals(dto.getFinalMark(), entity.getFinalMark());
    }

    @Test
    void toDto_ShouldHandleNullTraineeshipPosition() {
        Evaluation entity = new Evaluation();
        entity.setTraineeshipPosition(null);
        EvaluationDto dto = EvaluationMapper.toDto(entity);
        assertNull(dto.getTraineeshipPosition());
    }

    @Test
    void toEntity_ShouldHandleNullTraineeshipPosition() {
        EvaluationDto dto = new EvaluationDto();
        dto.setTraineeshipPosition(null);
        Evaluation entity = EvaluationMapper.toEntity(dto);
        assertNull(entity.getTraineeshipPosition());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10})
    void toDto_ShouldHandleVariousRatingValues(int rating) {
        Evaluation entity = new Evaluation();
        entity.setProfessorMotivationRating(rating);
        entity.setProfessorEffectivenessRating(rating);
        entity.setProfessorEfficiencyRating(rating);
        entity.setCompanyMotivationRating(rating);
        entity.setCompanyEffectivenessRating(rating);
        entity.setCompanyEfficiencyRating(rating);

        EvaluationDto dto = EvaluationMapper.toDto(entity);

        assertEquals(rating, dto.getProfessorMotivationRating());
        assertEquals(rating, dto.getProfessorEffectivenessRating());
        assertEquals(rating, dto.getProfessorEfficiencyRating());
        assertEquals(rating, dto.getCompanyMotivationRating());
        assertEquals(rating, dto.getCompanyEffectivenessRating());
        assertEquals(rating, dto.getCompanyEfficiencyRating());
    }

    @ParameterizedTest
    @ValueSource(floats = {0.0f, 1.0f, 5.0f, 10.0f})
    void toEntity_ShouldHandleVariousRatingValues(float rating) {
        EvaluationDto dto = new EvaluationDto();
        dto.setProfessorMotivationRating((int) rating);
        dto.setProfessorEffectivenessRating((int) rating);
        dto.setProfessorEfficiencyRating((int) rating);
        dto.setCompanyMotivationRating((int) rating);
        dto.setCompanyEffectivenessRating((int) rating);
        dto.setCompanyEfficiencyRating((int) rating);

        Evaluation entity = EvaluationMapper.toEntity(dto);

        assertEquals((int) rating, entity.getProfessorMotivationRating());
        assertEquals((int) rating, entity.getProfessorEffectivenessRating());
        assertEquals((int) rating, entity.getProfessorEfficiencyRating());
        assertEquals((int) rating, entity.getCompanyMotivationRating());
        assertEquals((int) rating, entity.getCompanyEffectivenessRating());
        assertEquals((int) rating, entity.getCompanyEfficiencyRating());
    }

    @ParameterizedTest
    @NullSource
    void toDto_ShouldHandleNullRatings(Integer rating) {
        Evaluation entity = new Evaluation();
        entity.setProfessorMotivationRating(rating);
        entity.setProfessorEffectivenessRating(rating);
        entity.setProfessorEfficiencyRating(rating);
        entity.setCompanyMotivationRating(rating);
        entity.setCompanyEffectivenessRating(rating);
        entity.setCompanyEfficiencyRating(rating);

        EvaluationDto dto = EvaluationMapper.toDto(entity);

        assertNull(dto.getProfessorMotivationRating());
        assertNull(dto.getProfessorEffectivenessRating());
        assertNull(dto.getProfessorEfficiencyRating());
        assertNull(dto.getCompanyMotivationRating());
        assertNull(dto.getCompanyEffectivenessRating());
        assertNull(dto.getCompanyEfficiencyRating());
    }
}
