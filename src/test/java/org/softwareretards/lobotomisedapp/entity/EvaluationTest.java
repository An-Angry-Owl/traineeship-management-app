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
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = WavefrontProperties.Application.class)
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
    void validRatingsShouldNotTriggerValidationViolations() {
        evaluation.setProfStdMotivationRating(3);
        evaluation.setProfStdEffectivenessRating(4);
        evaluation.setProfStdEfficiencyRating(5);
        evaluation.setCompStdMotivationRating(2);
        evaluation.setCompStdEffectivenessRating(1);
        evaluation.setCompStdEfficiencyRating(5);

        Set<ConstraintViolation<Evaluation>> violations = validator.validate(evaluation);
        assertTrue(violations.isEmpty());
    }

    @Test
    void nullRatingsShouldNotTriggerValidationViolations() {
        evaluation.setProfStdMotivationRating(null);
        evaluation.setProfStdEffectivenessRating(null);
        evaluation.setProfStdEfficiencyRating(null);
        evaluation.setCompStdMotivationRating(null);
        evaluation.setCompStdEffectivenessRating(null);
        evaluation.setCompStdEfficiencyRating(null);

        Set<ConstraintViolation<Evaluation>> violations = validator.validate(evaluation);
        assertTrue(violations.isEmpty());
    }

    @Test
    void minimumAndMaximumRatingsShouldBeValid() {
        evaluation.setProfStdMotivationRating(1);
        evaluation.setProfStdEffectivenessRating(5);
        evaluation.setProfStdEfficiencyRating(1);
        evaluation.setCompStdMotivationRating(5);
        evaluation.setCompStdEffectivenessRating(1);
        evaluation.setCompStdEfficiencyRating(5);

        Set<ConstraintViolation<Evaluation>> violations = validator.validate(evaluation);
        assertTrue(violations.isEmpty());
    }

    @Test
    void negativeAndOverMaxRatingsShouldTriggerValidationViolations() {
        evaluation.setProfStdMotivationRating(-5);
        evaluation.setProfStdEffectivenessRating(100);
        evaluation.setProfStdEfficiencyRating(0);
        evaluation.setCompStdMotivationRating(6);
        evaluation.setCompStdEffectivenessRating(-1);
        evaluation.setCompStdEfficiencyRating(10);

        Set<ConstraintViolation<Evaluation>> violations = validator.validate(evaluation);
        assertEquals(6, violations.size());
    }

    @Test
    void defaultFinalMarkShouldBePending() {
        assertEquals(FinalMark.PENDING, evaluation.getFinalMark());
    }

    @Test
    void canSetAndGetAllProfessorAndCompanyRatings() {
        evaluation.setProfStdMotivationRating(2);
        evaluation.setProfStdEffectivenessRating(3);
        evaluation.setProfStdEfficiencyRating(4);
        evaluation.setCompStdMotivationRating(5);
        evaluation.setCompStdEffectivenessRating(1);
        evaluation.setCompStdEfficiencyRating(2);

        assertEquals(2, evaluation.getProfStdMotivationRating());
        assertEquals(3, evaluation.getProfStdEffectivenessRating());
        assertEquals(4, evaluation.getProfStdEfficiencyRating());
        assertEquals(5, evaluation.getCompStdMotivationRating());
        assertEquals(1, evaluation.getCompStdEffectivenessRating());
        assertEquals(2, evaluation.getCompStdEfficiencyRating());
    }
}
