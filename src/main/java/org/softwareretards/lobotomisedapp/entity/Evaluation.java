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


    // Professor-specific ratings
    @Min(1) @Max(5)
    @Column(name = "prof_std_motivation_rating")
    private Integer profStdMotivationRating;

    @Min(1) @Max(5)
    @Column(name = "prof_std_effectiveness_rating")
    private Integer profStdEffectivenessRating;

    @Min(1) @Max(5)
    @Column(name = "prof_std_efficiency_rating")
    private Integer profStdEfficiencyRating;

    @Min(1) @Max(5)
    @Column(name = "prof_comp_facilities_rating")
    private Integer profCompFacilitiesRating;

    @Min(1) @Max(5)
    @Column(name = "prof_comp_guidance_rating")
    private Integer profCompGuidanceRating;


    // Company-specific ratings
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
                // Professor-specific ratings
                ", profStdMotivationRating=" + profStdMotivationRating +
                ", profStdEffectivenessRating=" + profStdEffectivenessRating +
                ", profStdEfficiencyRating=" + profStdEfficiencyRating +
                ", profCompFacilitiesRating=" + profCompFacilitiesRating +
                ", profCompGuidanceRating=" + profCompGuidanceRating +
                // Company-specific ratings
                ", compStdMotivationRating=" + compStdMotivationRating +
                ", compStdEffectivenessRating=" + compStdEffectivenessRating +
                ", compStdEfficiencyRating=" + compStdEfficiencyRating +
                ", finalMark=" + finalMark +
                '}';
    }
}