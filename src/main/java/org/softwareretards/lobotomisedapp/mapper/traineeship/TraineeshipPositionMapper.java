package org.softwareretards.lobotomisedapp.mapper.traineeship;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.mapper.user.CompanyMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.mapper.LogbookEntryMapper;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraineeshipPositionMapper {

    public static TraineeshipPositionDto toDto(TraineeshipPosition entity) {
        if (entity == null) {
            return null;
        }
        TraineeshipPositionDto dto = new TraineeshipPositionDto();
        dto.setId(entity.getId());
        // TODO: add a name in the proper way
        dto.setPositionName(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDescription(entity.getDescription());
        dto.setRequiredSkills(entity.getRequiredSkills());
        dto.setTopics(entity.getTopics());
        dto.setStatus(entity.getStatus());

        // Map nested objects using static methods
        dto.setCompany(CompanyMapper.toDto(entity.getCompany()));
        dto.setStudent(StudentMapper.toDto(entity.getStudent()));
        dto.setProfessor(ProfessorMapper.toDto(entity.getProfessor()));

        // Map collections for logbook entries if not null
        if (entity.getLogbookEntries() != null) {
            List<LogbookEntryDto> logbookEntryDtos = entity.getLogbookEntries().stream()
                    .map(LogbookEntryMapper::toDto)
                    .collect(Collectors.toList());
            dto.setLogbookEntries(logbookEntryDtos);
        }

        // Map one-to-one Evaluation if present
        dto.setEvaluation(EvaluationMapper.toDto(entity.getEvaluation()));

        return dto;
    }

    public static TraineeshipPosition toEntity(TraineeshipPositionDto dto) {
        if (dto == null) {
            return null;
        }
        TraineeshipPosition entity = new TraineeshipPosition();
        // Map company from DTO
        if (dto.getCompany() != null) {
            Company company = new Company();
            company.setId(dto.getCompany().getUserDto().getId());
            entity.setCompany(company);
        }
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());
        entity.setRequiredSkills(dto.getRequiredSkills());
        entity.setTopics(dto.getTopics());
        entity.setStatus(dto.getStatus());

        // Map nested objects using static methods
        entity.setCompany(CompanyMapper.toEntity(dto.getCompany()));
        entity.setStudent(StudentMapper.toEntity(dto.getStudent()));
        entity.setProfessor(ProfessorMapper.toEntity(dto.getProfessor()));

        // Map collections for logbook entries if available
        if (dto.getLogbookEntries() != null) {
            List<LogbookEntry> logbookEntries = dto.getLogbookEntries().stream()
                    .map(LogbookEntryMapper::toEntity)
                    .collect(Collectors.toList());
            entity.setLogbookEntries(logbookEntries);
        }

        // Map one-to-one Evaluation if present
        entity.setEvaluation(EvaluationMapper.toEntity(dto.getEvaluation()));

        return entity;
    }

    public static List<TraineeshipPositionDto> toDtoList(List<TraineeshipPosition> positions) {
        if (positions == null) {
            return null;
        }
        return positions.stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());
    }
}