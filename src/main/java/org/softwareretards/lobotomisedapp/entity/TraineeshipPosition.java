package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traineeship_positions")
@Getter @Setter @NoArgsConstructor
public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "topics", columnDefinition = "TEXT")
    private String topics;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TraineeshipStatus status = TraineeshipStatus.OPEN;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogbookEntry> logbookEntries = new ArrayList<>();

    @OneToOne(mappedBy = "traineeshipPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    private Evaluation evaluation;

    @Override
    public String toString() {
        return "TraineeshipPosition{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", requiredSkills='" + requiredSkills + '\'' +
                ", topics='" + topics + '\'' +
                ", status=" + status +
                '}';
    }
}