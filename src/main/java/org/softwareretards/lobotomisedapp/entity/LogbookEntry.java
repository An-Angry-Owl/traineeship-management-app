package org.softwareretards.lobotomisedapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.sql.Timestamp;

@Entity
@Table(name = "logbook_entries")
@Getter
@Setter
@NoArgsConstructor
public class LogbookEntry {

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

    @Column(name = "entry_date", nullable = false, updatable = false)
    private Timestamp entryDate;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public LogbookEntry(Student student,
                        TraineeshipPosition position,
                        String content) {
        this.student = student;
        this.position = position;
        this.entryDate = new Timestamp(System.currentTimeMillis());
        this.content = content;
    }

    @Override
    public String toString() {
        return "LogbookEntry{" +
                "id=" + id +
                ", student=" + student +
                ", position=" + position +
                ", entryDate=" + entryDate +
                ", content='" + content + '\'' +
                '}';
    }
}
