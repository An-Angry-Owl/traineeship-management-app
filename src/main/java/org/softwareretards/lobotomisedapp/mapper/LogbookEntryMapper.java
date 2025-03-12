package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionSummaryDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogbookEntryMapper {

    private final StudentMapper studentMapper;

    @Autowired
    public LogbookEntryMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public LogbookEntryDto toDto(LogbookEntry entity) {
        if (entity == null) {
            return null;
        }
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(entity.getId());
        dto.setStudent(studentMapper.toDto(entity.getStudent()));
        dto.setEntryDate(entity.getEntryDate());
        dto.setContent(entity.getContent());

        // Map to Summary DTO to avoid circular dependency
        if (entity.getPosition() != null) {
            TraineeshipPositionSummaryDto positionSummary = new TraineeshipPositionSummaryDto();
            positionSummary.setId(entity.getPosition().getId());
            positionSummary.setDescription(entity.getPosition().getDescription());
            positionSummary.setStatus(entity.getPosition().getStatus());
            dto.setPosition(positionSummary);
        }

        return dto;
    }

    public LogbookEntry toEntity(LogbookEntryDto dto) {
        if (dto == null) {
            return null;
        }
        LogbookEntry entity = new LogbookEntry();
        entity.setId(dto.getId());
        entity.setStudent(studentMapper.toEntity(dto.getStudent()));
        entity.setEntryDate(dto.getEntryDate());
        entity.setContent(dto.getContent());

        // No need to map full position entity (handled separately)
        return entity;
    }
}
