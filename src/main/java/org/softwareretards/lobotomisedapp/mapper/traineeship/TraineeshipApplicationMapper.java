package org.softwareretards.lobotomisedapp.mapper.traineeship;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.stereotype.Component;

@Component
public class TraineeshipApplicationMapper {

    public static TraineeshipApplicationDto toDto(TraineeshipApplication entity) {
        if (entity == null) {
            return null;
        }
        TraineeshipApplicationDto dto = new TraineeshipApplicationDto();
        dto.setId(entity.getId());
        dto.setApplicationDate(entity.getApplicationDate());
        dto.setStatus(entity.getStatus());
        // Map nested objects using static methods
        dto.setStudent(StudentMapper.toDto(entity.getStudent()));
        return dto;
    }

    public static TraineeshipApplication toEntity(TraineeshipApplicationDto dto) {
        if (dto == null) {
            return null;
        }
        TraineeshipApplication entity = new TraineeshipApplication();
        entity.setId(dto.getId());
        entity.setApplicationDate(dto.getApplicationDate());
        entity.setStatus(dto.getStatus());
        // Map nested objects using static methods
        entity.setStudent(StudentMapper.toEntity(dto.getStudent()));
        return entity;
    }
}