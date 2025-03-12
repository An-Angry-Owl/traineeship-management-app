package org.softwareretards.lobotomisedapp.mapper.traineeship;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraineeshipApplicationMapper {

    private final StudentMapper studentMapper;
    private final TraineeshipPositionMapper traineeshipPositionMapper;

    @Autowired
    public TraineeshipApplicationMapper(StudentMapper studentMapper,
                                        TraineeshipPositionMapper traineeshipPositionMapper) {
        this.studentMapper = studentMapper;
        this.traineeshipPositionMapper = traineeshipPositionMapper;
    }

    public TraineeshipApplicationDto toDto(TraineeshipApplication entity) {
        if (entity == null) {
            return null;
        }
        TraineeshipApplicationDto dto = new TraineeshipApplicationDto();
        dto.setId(entity.getId());
        dto.setApplicationDate(entity.getApplicationDate());
        dto.setStatus(entity.getStatus());
        // Map nested objects using their corresponding mappers
        dto.setStudent(studentMapper.toDto(entity.getStudent()));
        dto.setPosition(traineeshipPositionMapper.toDto(entity.getPosition()));
        return dto;
    }

    public TraineeshipApplication toEntity(TraineeshipApplicationDto dto) {
        if (dto == null) {
            return null;
        }
        TraineeshipApplication entity = new TraineeshipApplication();
        entity.setId(dto.getId());
        entity.setApplicationDate(dto.getApplicationDate());
        entity.setStatus(dto.getStatus());
        // Map nested objects using their corresponding mappers
        entity.setStudent(studentMapper.toEntity(dto.getStudent()));
        entity.setPosition(traineeshipPositionMapper.toEntity(dto.getPosition()));
        return entity;
    }
}
