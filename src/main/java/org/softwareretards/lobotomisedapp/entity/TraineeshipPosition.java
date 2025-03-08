package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.sql.Date;

/**
 * Entity class representing a Traineeship Positions.
 * <p>
 * It includes the required fields and default values for variables such as Student, Professor and Status
 * </p>
 */

@Entity
@Table(name = "traineeship_positions")
@Getter
@Setter
@NoArgsConstructor

public class TraineeshipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "required_skills", nullable = false, columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "topics", nullable = false, columnDefinition = "TEXT")
    private String topics;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TraineeshipStatus status = TraineeshipStatus.OPEN;



    public TraineeshipPosition(Company company, Student student, Professor professor, Date startDate, Date endDate, String description, String requiredSkills, String topics, TraineeshipStatus status) {
        this.company = company;
        this.student = student;
        this.professor = professor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.topics = topics;
        this.status = status != null ? status : TraineeshipStatus.OPEN; // Default to OPEN if status is null
    }

    @Override
    public String toString() {
        return "LogbookEntry{" +
                "id=" + id +
                ", company=" + company +
                ", student=" + student +
                ", professor=" + professor +
                ", startDate='" + startDate + '\'' +
                ", endDate=" + endDate +
                ", description=" + description  + '\''+
                ", requiredSkills=" + requiredSkills  + '\''+
                ", topics=" + topics  + '\''+
                ", status=" + status +
                '}';
    }

}
