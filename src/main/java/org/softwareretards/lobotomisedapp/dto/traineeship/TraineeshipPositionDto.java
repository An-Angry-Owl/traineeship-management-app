package org.softwareretards.lobotomisedapp.dto.traineeship;

import lombok.Data;
import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeshipPositionDto {
    private Long id;
    private CompanyDto company;
    private StudentDto student;
    private ProfessorDto professor;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String requiredSkills;
    private String topics;
    private TraineeshipStatus status;
    private List<LogbookEntryDto> logbookEntries;
    private EvaluationDto evaluation;
}
