package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogbookEntryMapper {

    public static LogbookEntryDto toDto(LogbookEntry entity) {
        if (entity == null) {
            return null;
        }

        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(entity.getId());
        dto.setEntryDate(entity.getEntryDate());
        dto.setContent(entity.getContent());

        // Handle student mapping safely
        if (entity.getStudent() != null) {
            // Create new StudentDto and UserDto
            UserDto userDto = new UserDto();
            userDto.setId(entity.getStudent().getId());
            userDto.setUsername(entity.getStudent().getUsername());

            // Create and set student DTO
            dto.setStudent(new StudentDto());
            dto.getStudent().setUserDto(userDto);
        }

        // Handle position mapping safely
        if (entity.getPosition() != null) {
            dto.setPosition(new TraineeshipPositionDto());
            dto.getPosition().setId(entity.getPosition().getId());
            dto.getPosition().setPositionName(entity.getPosition().getDescription());
        }

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

    public static List<LogbookEntryDto> toDtoList(List<LogbookEntry> entries) {
        if (entries == null) {
            return null;
        }
        return entries.stream()
                .map(LogbookEntryMapper::toDto)
                .toList();
    }
}