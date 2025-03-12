package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.stereotype.Component;

@Component
public class LogbookEntryMapper {

    public static LogbookEntryDto toDto(LogbookEntry entity) {
        if (entity == null) {
            return null;
        }
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(entity.getId());
        dto.setStudent(StudentMapper.toDto(entity.getStudent())); // Use static method
        dto.setPosition(TraineeshipPositionMapper.toDto(entity.getPosition())); // Use static method
        dto.setEntryDate(entity.getEntryDate());
        dto.setContent(entity.getContent());
        return dto;
    }

    public static LogbookEntry toEntity(LogbookEntryDto dto) {
        if (dto == null) {
            return null;
        }
        LogbookEntry entity = new LogbookEntry();
        entity.setId(dto.getId());
        entity.setStudent(StudentMapper.toEntity(dto.getStudent())); // Use static method
        entity.setPosition(TraineeshipPositionMapper.toEntity(dto.getPosition())); // Use static method
        entity.setEntryDate(dto.getEntryDate());
        entity.setContent(dto.getContent());
        return entity;
    }
}