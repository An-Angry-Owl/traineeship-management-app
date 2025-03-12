package org.softwareretards.lobotomisedapp.mapper;

import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogbookEntryMapper {

    private final StudentMapper studentMapper;
    private final TraineeshipPositionMapper traineeshipPositionMapper;

    @Autowired
    public LogbookEntryMapper(StudentMapper studentMapper,
                              TraineeshipPositionMapper traineeshipPositionMapper) {
        this.studentMapper = studentMapper;
        this.traineeshipPositionMapper = traineeshipPositionMapper;
    }

    public LogbookEntryDto toDto(LogbookEntry entity) {
        if (entity == null) {
            return null;
        }
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(entity.getId());
        dto.setStudent(studentMapper.toDto(entity.getStudent()));
        dto.setPosition(traineeshipPositionMapper.toDto(entity.getPosition()));
        dto.setEntryDate(entity.getEntryDate());
        dto.setContent(entity.getContent());
        return dto;
    }

    public LogbookEntry toEntity(LogbookEntryDto dto) {
        if (dto == null) {
            return null;
        }
        LogbookEntry entity = new LogbookEntry();
        entity.setId(dto.getId());
        entity.setStudent(studentMapper.toEntity(dto.getStudent()));
        entity.setPosition(traineeshipPositionMapper.toEntity(dto.getPosition()));
        entity.setEntryDate(dto.getEntryDate());
        entity.setContent(dto.getContent());
        return entity;
    }
}