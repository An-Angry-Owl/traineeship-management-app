package org.softwareretards.lobotomisedapp.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = WavefrontProperties.Application.class)
class LogbookEntryMapperTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @InjectMocks
    private LogbookEntryMapper logbookEntryMapper;

    @AfterEach
    void tearDown() {
        studentMapper = null;
        traineeshipPositionMapper = null;
        logbookEntryMapper = null;
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        LogbookEntryDto result = LogbookEntryMapper.toDto(null);
        assertNull(result);
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        LogbookEntry result = LogbookEntryMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LogbookEntry entity = new LogbookEntry();
        entity.setId(1L);
        entity.setEntryDate(Timestamp.valueOf(Timestamp.valueOf("2023-05-15 10:00:00").toLocalDateTime()));
        entity.setContent("Test content");

        LogbookEntryDto dto = LogbookEntryMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEntryDate(), dto.getEntryDate());
        assertEquals(entity.getContent(), dto.getContent());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(1L);
        dto.setEntryDate(Timestamp.valueOf(Timestamp.valueOf("2023-05-15 10:00:00").toLocalDateTime()));
        dto.setContent("Test content");

        LogbookEntry entity = LogbookEntryMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getEntryDate(), entity.getEntryDate());
        assertEquals(dto.getContent(), entity.getContent());
    }

    @Test
    void toDto_ShouldHandleNullStudentAndPosition() {
        LogbookEntry entity = new LogbookEntry();
        entity.setId(1L);
        entity.setEntryDate(Timestamp.valueOf(Timestamp.valueOf("2023-05-15 10:00:00").toLocalDateTime()));
        entity.setContent("Test content");
        entity.setStudent(null);
        entity.setPosition(null);

        LogbookEntryDto dto = LogbookEntryMapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getStudent());
        assertNull(dto.getPosition());
    }

    @Test
    void toEntity_ShouldHandleNullStudentAndPosition() {
        LogbookEntryDto dto = new LogbookEntryDto();
        dto.setId(1L);
        dto.setEntryDate(Timestamp.valueOf(Timestamp.valueOf("2023-05-15 10:00:00").toLocalDateTime()));
        dto.setContent("Test content");
        dto.setStudent(null);
        dto.setPosition(null);

        LogbookEntry entity = LogbookEntryMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getStudent());
        assertNull(entity.getPosition());
    }
}
