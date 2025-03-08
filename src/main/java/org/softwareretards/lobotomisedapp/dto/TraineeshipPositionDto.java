package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TraineeshipPositionDto {
    private Long id;
    private Company company;
    private Student student;
    private Professor professor;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String requiredSkills;
    private String topics;
    private TraineeshipStatus status;
    private List<LogbookEntry> logbookEntries;
    private Evaluation evaluation;
}
