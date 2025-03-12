package org.softwareretards.lobotomisedapp.entity.traineeship;

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

public class TraineeshipApplication{

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
    private Timestamp applicationDate; // moved to constructor

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING; // Best to set it here because it is static

    public TraineeshipApplication(Student student, TraineeshipPosition position){
        this.student = student;
        this.position = position;
        this.applicationDate = new Timestamp(System.currentTimeMillis()); // Best to set it here because it is dynamic (aka, every time it is called it is different)
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