package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import java.sql.Timestamp;

@Entity
@Table(name = "traineeship_applications")
@Getter
@Setter
@NoArgsConstructor
public class TraineeshipApplications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private TraineeshipPosition position;

    @Column(name = "application_date", updatable = false, nullable = false)
    private Timestamp applicationDate = new Timestamp(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    // Xriazomaste constractor? I suppose boroume na to doume kai meta afto
    public TraineeshipApplications(Student student, TraineeshipPosition position) {
        this.student = student;
        this.position = position;
    }

    @Override
    public String toString() {
        return "TraineeshipApplication{" +
                "id=" + id +
                ", student=" + student +
                ", position=" + position +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                '}';
    }
}