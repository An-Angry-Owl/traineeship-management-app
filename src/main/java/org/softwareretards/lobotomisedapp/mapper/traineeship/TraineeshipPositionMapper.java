package org.softwareretards.lobotomisedapp.mapper.traineeship;

import java.util.List;
import java.util.stream.Collectors;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.mapper.user.CompanyMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.mapper.LogbookEntryMapper;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraineeshipPositionMapper {

    private final CompanyMapper companyMapper;
    private final StudentMapper studentMapper;
    private final ProfessorMapper professorMapper;
    private final LogbookEntryMapper logbookEntryMapper;
    private final EvaluationMapper evaluationMapper;

    @Autowired
    public TraineeshipPositionMapper(CompanyMapper companyMapper,
                                     StudentMapper studentMapper,
                                     ProfessorMapper professorMapper,
                                     LogbookEntryMapper logbookEntryMapper,
                                     EvaluationMapper evaluationMapper) {
        this.companyMapper = companyMapper;
        this.studentMapper = studentMapper;
        this.professorMapper = professorMapper;
        this.logbookEntryMapper = logbookEntryMapper;
        this.evaluationMapper = evaluationMapper;
    }

    public TraineeshipPositionDto toDto(TraineeshipPosition entity) {
        if (entity == null) {
            return null;
        }
        TraineeshipPositionDto dto = new TraineeshipPositionDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());
        dto.setRequiredSkills(entity.getRequiredSkills());
        dto.setTopics(entity.getTopics());
        dto.setStatus(entity.getStatus());

        // Map nested objects using their respective mappers
        dto.setCompany(companyMapper.toDto(entity.getCompany()));
        dto.setStudent(studentMapper.toDto(entity.getStudent()));
        dto.setProfessor(professorMapper.toDto(entity.getProfessor()));

        // Map collections for logbook entries if not null
        if (entity.getLogbookEntries() != null) {
            List<?> logbookEntryDtos = entity.getLogbookEntries().stream()
                    .map(logbookEntryMapper::toDto)
                    .collect(Collectors.toList());
            dto.setLogbookEntries(logbookEntryDtos);
        }

        // Map one-to-one Evaluation if present
        dto.setEvaluation(evaluationMapper.toDto(entity.getEvaluation()));

        return dto;
    }

    public TraineeshipPosition toEntity(TraineeshipPositionDto dto) {
        if (dto == null) {
            return null;
        }
        TraineeshipPosition entity = new TraineeshipPosition();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());
        entity.setRequiredSkills(dto.getRequiredSkills());
        entity.setTopics(dto.getTopics());
        entity.setStatus(dto.getStatus());

        // Map nested objects using their respective mappers
        entity.setCompany(companyMapper.toEntity(dto.getCompany()));
        entity.setStudent(studentMapper.toEntity(dto.getStudent()));
        entity.setProfessor(professorMapper.toEntity(dto.getProfessor()));

        // Map collections for logbook entries if available
        if (dto.getLogbookEntries() != null) {
            List<?> logbookEntries = dto.getLogbookEntries().stream()
                    .map(logbookEntryMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setLogbookEntries(logbookEntries);
        }

        // Map one-to-one Evaluation if present
        entity.setEvaluation(evaluationMapper.toEntity(dto.getEvaluation()));

        return entity;
    }
}
