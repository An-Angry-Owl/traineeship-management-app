package org.softwareretards.lobotomisedapp.mapper.traineeship;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TraineeshipApplicationMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(TraineeshipApplicationMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(TraineeshipApplicationMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");

        StudentDto studentDto = new StudentDto();
        studentDto.setUserDto(userDto);
        studentDto.getUserDto().setId(2L);

        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setId(3L);
        positionDto.setDescription("description");

        TraineeshipApplication entity = new TraineeshipApplication();
        entity.setId(1L);
        entity.setApplicationDate(Timestamp.valueOf("2023-05-15 10:00:00"));
        entity.setStatus(ApplicationStatus.PENDING);
        entity.setStudent(new Student());
        entity.getStudent().setId(2L);
        entity.setPosition(new TraineeshipPosition());
        entity.getPosition().setId(3L);

        TraineeshipApplicationDto dto = TraineeshipApplicationMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getApplicationDate(), dto.getApplicationDate());
        assertEquals(entity.getStatus(), dto.getStatus());
        assertNotNull(dto.getStudent());
        assertEquals(2L, dto.getStudent().getUserDto().getId());
        assertNotNull(dto.getPosition());
        assertEquals(3L, dto.getPosition().getId());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        StudentDto studentDto = new StudentDto();
        studentDto.setUserDto(userDto);
        studentDto.getUserDto().setId(2L);
        studentDto.getUserDto().setUsername("username");

        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setId(3L);
        positionDto.setDescription("description");

        TraineeshipApplicationDto dto = new TraineeshipApplicationDto();
        dto.setId(1L);
        dto.setApplicationDate(Timestamp.valueOf("2023-05-15 10:00:00"));
        dto.setStatus(ApplicationStatus.PENDING);
        dto.setStudent(studentDto);
        dto.setPosition(positionDto);

        TraineeshipApplication entity = TraineeshipApplicationMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getApplicationDate(), entity.getApplicationDate());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertNotNull(entity.getStudent());
        assertEquals(2L, entity.getStudent().getId());
        assertNotNull(entity.getPosition());
        assertEquals(3L, entity.getPosition().getId());
    }

    @Test
    void toDto_ShouldHandleNullNestedObjects() {
        TraineeshipApplication entity = new TraineeshipApplication();
        entity.setStudent(null);
        entity.setPosition(null);

        TraineeshipApplicationDto dto = TraineeshipApplicationMapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getStudent());
        assertNull(dto.getPosition());
    }

    @Test
    void toEntity_ShouldHandleNullNestedObjects() {
        TraineeshipApplicationDto dto = new TraineeshipApplicationDto();
        dto.setStudent(null);
        dto.setPosition(null);

        TraineeshipApplication entity = TraineeshipApplicationMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getStudent());
        assertNull(entity.getPosition());
    }

    @Test
    void toDto_ShouldHandleAllStatusValues() {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            TraineeshipApplication entity = new TraineeshipApplication();
            entity.setStatus(status);

            TraineeshipApplicationDto dto = TraineeshipApplicationMapper.toDto(entity);

            assertEquals(status, dto.getStatus());
        }
    }

    @Test
    void toEntity_ShouldHandleAllStatusValues() {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            TraineeshipApplicationDto dto = new TraineeshipApplicationDto();
            dto.setStatus(status);

            TraineeshipApplication entity = TraineeshipApplicationMapper.toEntity(dto);

            assertEquals(status, entity.getStatus());
        }
    }
}
