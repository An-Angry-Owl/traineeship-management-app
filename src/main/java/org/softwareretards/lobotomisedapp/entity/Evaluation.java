package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "traineeship_position_id", nullable = false, unique = true)
    private TraineeshipPosition traineeshipPosition;

    // Professor ratings (1-5 scale)
    @Column(name = "prof_motivation_rating")
    private Integer profMotivationRating;

    @Column(name = "prof_effectiveness_rating")
    private Integer profEffectivenessRating;

    @Column(name = "prof_efficiency_rating")
    private Integer profEfficiencyRating;

    // Company ratings (1-5 scale)
    @Column(name = "comp_motivation_rating")
    private Integer compMotivationRating;

    @Column(name = "comp_effectiveness_rating")
    private Integer compEffectivenessRating;

    @Column(name = "comp_efficiency_rating")
    private Integer compEfficiencyRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "final_mark")
    private FinalMark finalMark = FinalMark.PENDING;

    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", profMotivationRating=" + profMotivationRating +
                ", profEffectivenessRating=" + profEffectivenessRating +
                ", profEfficiencyRating=" + profEfficiencyRating +
                ", compMotivationRating=" + compMotivationRating +
                ", compEffectivenessRating=" + compEffectivenessRating +
                ", compEfficiencyRating=" + compEfficiencyRating +
                ", finalMark=" + finalMark +
                '}';
    }
}