package org.softwareretards.lobotomisedapp.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EvaluationTest {

    @InjectMocks
    private Evaluation evaluation;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        evaluation = new Evaluation();
    }

    @AfterEach
    void tearDown() {
        evaluation = null;
    }

    @Test
    void testDefaultConstructor() {
        assertNull(evaluation.getId());
        assertNull(evaluation.getTraineeshipPosition());
        assertNull(evaluation.getProfessorMotivationRating());
        assertNull(evaluation.getProfessorEffectivenessRating());
        assertNull(evaluation.getProfessorEfficiencyRating());
        assertNull(evaluation.getCompanyMotivationRating());
        assertNull(evaluation.getCompanyEffectivenessRating());
        assertNull(evaluation.getCompanyEfficiencyRating());
        assertEquals(FinalMark.PENDING, evaluation.getFinalMark());
    }

    @Test
    void testGettersAndSetters() {
        evaluation.setId(1L);
        assertEquals(1L, evaluation.getId());

        TraineeshipPosition traineeshipPosition = mock(TraineeshipPosition.class);
        evaluation.setTraineeshipPosition(traineeshipPosition);
        assertEquals(traineeshipPosition, evaluation.getTraineeshipPosition());

        evaluation.setProfessorMotivationRating(5);
        assertEquals(5, evaluation.getProfessorMotivationRating());

        evaluation.setProfessorEffectivenessRating(4);
        assertEquals(4, evaluation.getProfessorEffectivenessRating());

        evaluation.setProfessorEfficiencyRating(3);
        assertEquals(3, evaluation.getProfessorEfficiencyRating());

        evaluation.setCompanyMotivationRating(2);
        assertEquals(2, evaluation.getCompanyMotivationRating());

        evaluation.setCompanyEffectivenessRating(1);
        assertEquals(1, evaluation.getCompanyEffectivenessRating());

        evaluation.setCompanyEfficiencyRating(5);
        assertEquals(5, evaluation.getCompanyEfficiencyRating());

        evaluation.setFinalMark(FinalMark.PASS);
        assertEquals(FinalMark.PASS, evaluation.getFinalMark());
    }

    @Test
    void testValidationAnnotations() {
        evaluation.setProfessorMotivationRating(0);
        evaluation.setProfessorEffectivenessRating(6);
        evaluation.setCompanyMotivationRating(-1);
        evaluation.setCompanyEffectivenessRating(10);

        Set<ConstraintViolation<Evaluation>> violations = validator.validate(evaluation);
        assertEquals(4, violations.size());
    }

    @Test
    void testToString() {
        evaluation.setId(1L);
        evaluation.setProfessorMotivationRating(5);
        evaluation.setProfessorEffectivenessRating(4);
        evaluation.setProfessorEfficiencyRating(3);
        evaluation.setCompanyMotivationRating(2);
        evaluation.setCompanyEffectivenessRating(1);
        evaluation.setCompanyEfficiencyRating(5);
        evaluation.setFinalMark(FinalMark.PASS);

        String expectedToString = "Evaluation{" +
                "id=1" +
                ", profMotivationRating=5" +
                ", profEffectivenessRating=4" +
                ", profEfficiencyRating=3" +
                ", compMotivationRating=2" +
                ", compEffectivenessRating=1" +
                ", compEfficiencyRating=5" +
                ", finalMark=PASS" +
                '}';

        assertEquals(expectedToString, evaluation.toString());
    }
}
