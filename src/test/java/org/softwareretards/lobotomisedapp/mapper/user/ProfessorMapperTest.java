package org.softwareretards.lobotomisedapp.mapper.user;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.Professor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenProfessorIsNull() {
        assertNull(ProfessorMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(ProfessorMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Professor professor = new Professor();
        professor.setId(1L);
        professor.setUsername("prof1");
        professor.setRole(Role.valueOf("PROFESSOR"));
        professor.setEnabled(true);
        professor.setCreatedAt(Timestamp.valueOf(now));
        professor.setUpdatedAt(Timestamp.valueOf(now));
        professor.setProfessorName("Dr. Smith");
        professor.setInterests(String.valueOf(List.of("AI", "Machine Learning")));

        ProfessorDto dto = ProfessorMapper.toDto(professor);

        assertNotNull(dto);
        assertEquals(professor.getId(), dto.getUserDto().getId());
        assertEquals(professor.getUsername(), dto.getUserDto().getUsername());
        assertEquals(professor.getRole(), dto.getUserDto().getRole());
        assertEquals(professor.isEnabled(), dto.getUserDto().isEnabled());
        assertEquals(professor.getCreatedAt(), dto.getUserDto().getCreatedAt());
        assertEquals(professor.getUpdatedAt(), dto.getUserDto().getUpdatedAt());
        assertEquals(professor.getProfessorName(), dto.getFullName());
        assertEquals(professor.getInterests(), dto.getInterests());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("prof1");
        userDto.setRole(Role.valueOf("PROFESSOR"));
        userDto.setEnabled(true);
        userDto.setCreatedAt(Timestamp.valueOf(now));
        userDto.setUpdatedAt(Timestamp.valueOf(now));

        ProfessorDto dto = new ProfessorDto();
        dto.setUserDto(userDto);
        dto.setFullName("Dr. Smith");
        dto.setInterests(String.valueOf(List.of("AI", "Machine Learning")));

        Professor professor = ProfessorMapper.toEntity(dto);

        assertNotNull(professor);
        assertEquals(dto.getUserDto().getId(), professor.getId());
        assertEquals(dto.getUserDto().getUsername(), professor.getUsername());
        assertEquals(dto.getUserDto().getRole(), professor.getRole());
        assertEquals(dto.getUserDto().isEnabled(), professor.isEnabled());
        assertEquals(dto.getUserDto().getCreatedAt(), professor.getCreatedAt());
        assertEquals(dto.getUserDto().getUpdatedAt(), professor.getUpdatedAt());
        assertEquals(dto.getFullName(), professor.getProfessorName());
        assertEquals(dto.getInterests(), professor.getInterests());
    }

    @Test
    void toDto_ShouldHandleNullFields() {
        Professor professor = new Professor();
        professor.setId(1L);
        professor.setUsername("prof1");
        professor.setProfessorName(null);
        professor.setInterests(null);

        ProfessorDto dto = ProfessorMapper.toDto(professor);

        assertNotNull(dto);
        assertNull(dto.getFullName());
        assertNull(dto.getInterests());
    }

    @Test
    void toEntity_ShouldHandleNullFields() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("prof1");

        ProfessorDto dto = new ProfessorDto();
        dto.setUserDto(userDto);
        dto.setFullName(null);
        dto.setInterests(null);

        Professor professor = ProfessorMapper.toEntity(dto);

        assertNotNull(professor);
        assertNull(professor.getProfessorName());
        assertNull(professor.getInterests());
    }
}
