package org.softwareretards.lobotomisedapp.dto.traineeship;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    @Getter @Setter private Long id;
    @Getter @Setter private CompanyDto company;
    @Getter @Setter private StudentDto student;
    @Getter @Setter private ProfessorDto professor;
    @Getter @Setter private LocalDate startDate;
    @Getter @Setter private LocalDate endDate;
    @Getter @Setter private String description;
    @Getter @Setter private String requiredSkills;
    @Getter @Setter private String topics;
    @Getter @Setter private TraineeshipStatus status;
    @Getter @Setter private List<LogbookEntryDto> logbookEntries;
    @Getter @Setter private EvaluationDto evaluation;
}
