package org.softwareretards.lobotomisedapp.dto;
import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TraineeshipPositionDto {
    private int id;
    private int company_id;
    private int trainee_id;
    private int profession_id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String requiredSkills;
    private String topics;
    private TraineeshipStatus status;
    private List<LogbookEntry> logbookEntries;
    private Evaluation evaluation;
}
