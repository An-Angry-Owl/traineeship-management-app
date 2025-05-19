package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;

/**
 * Represents an evaluation of a traineeship position.
 * This entity is used to store the evaluation details for a specific traineeship position.
 * It includes ratings for both the professor and the company, as well as a final mark.
 */
@Entity
@Table(name = "evaluations")
@Getter @Setter @NoArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "traineeship_position_id", nullable = false, unique = true)
    private TraineeshipPosition traineeshipPosition;

    @Min(1) @Max(5)
    @Column(name = "prof_motivation_rating")
    private Integer professorMotivationRating;

    @Min(1) @Max(5)
    @Column(name = "prof_effectiveness_rating")
    private Integer professorEffectivenessRating;

    @Min(1) @Max(5)
    @Column(name = "prof_efficiency_rating")
    private Integer professorEfficiencyRating;

    @Min(1) @Max(5)
    @Column(name = "comp_motivation_rating")
    private Integer companyMotivationRating;

    @Min(1) @Max(5)
    @Column(name = "comp_effectiveness_rating")
    private Integer companyEffectivenessRating;

    @Min(1) @Max(5)
    @Column(name = "comp_efficiency_rating")
    private Integer companyEfficiencyRating;

    @Min(1) @Max(5)
    @Column(name = "comp_std_motivation_rating")
    private Integer compStdMotivationRating;

    @Min(1) @Max(5)
    @Column(name = "comp_std_effectiveness_rating")
    private Integer compStdEffectivenessRating;

    @Min(1) @Max(5)
    @Column(name = "comp_std_efficiency_rating")
    private Integer compStdEfficiencyRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "final_mark")
    private FinalMark finalMark = FinalMark.PENDING;

    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", profMotivationRating=" + professorMotivationRating +
                ", profEffectivenessRating=" + professorEffectivenessRating +
                ", profEfficiencyRating=" + professorEfficiencyRating +
                ", compMotivationRating=" + companyMotivationRating +
                ", compEffectivenessRating=" + companyEffectivenessRating +
                ", compEfficiencyRating=" + companyEfficiencyRating +
                ", finalMark=" + finalMark +
                '}';
    }
}