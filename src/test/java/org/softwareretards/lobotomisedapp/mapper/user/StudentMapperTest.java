package org.softwareretards.lobotomisedapp.mapper.user;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenStudentIsNull() {
        assertNull(StudentMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(StudentMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Student student = new Student();
        student.setId(1L);
        student.setUsername("student1");
        student.setRole(Role.valueOf("STUDENT"));
        student.setEnabled(true);
        student.setCreatedAt(Timestamp.valueOf(now));
        student.setUpdatedAt(Timestamp.valueOf(now));
        student.setFullName("John Doe");
        student.setUniversityId("U12345");
        student.setInterests(String.valueOf(List.of("AI", "Web Dev")));
        student.setSkills(String.valueOf(List.of("Java", "Python")));
        student.setPreferredLocation("Remote");

        StudentDto dto = StudentMapper.toDto(student);

        assertNotNull(dto);
        assertEquals(student.getId(), dto.getUserDto().getId());
        assertEquals(student.getUsername(), dto.getUserDto().getUsername());
        assertEquals(student.getRole(), dto.getUserDto().getRole());
        assertEquals(student.isEnabled(), dto.getUserDto().isEnabled());
        assertEquals(student.getCreatedAt(), dto.getUserDto().getCreatedAt());
        assertEquals(student.getUpdatedAt(), dto.getUserDto().getUpdatedAt());
        assertEquals(student.getFullName(), dto.getFullName());
        assertEquals(student.getUniversityId(), dto.getUniversityId());
        assertEquals(student.getInterests(), dto.getInterests());
        assertEquals(student.getSkills(), dto.getSkills());
        assertEquals(student.getPreferredLocation(), dto.getPreferredLocation());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("student1");
        userDto.setRole(Role.valueOf("STUDENT"));
        userDto.setEnabled(true);
        userDto.setCreatedAt(Timestamp.valueOf(now));
        userDto.setUpdatedAt(Timestamp.valueOf(now));

        StudentDto dto = new StudentDto();
        dto.setUserDto(userDto);
        dto.setFullName("John Doe");
        dto.setUniversityId("U12345");
        dto.setInterests(String.valueOf(List.of("AI", "Web Dev")));
        dto.setSkills(String.valueOf(List.of("Java", "Python")));
        dto.setPreferredLocation("Remote");

        Student student = StudentMapper.toEntity(dto);

        assertNotNull(student);
        assertEquals(dto.getUserDto().getId(), student.getId());
        assertEquals(dto.getUserDto().getUsername(), student.getUsername());
        assertEquals(dto.getUserDto().getRole(), student.getRole());
        assertEquals(dto.getUserDto().isEnabled(), student.isEnabled());
        assertEquals(dto.getUserDto().getCreatedAt(), student.getCreatedAt());
        assertEquals(dto.getUserDto().getUpdatedAt(), student.getUpdatedAt());
        assertEquals(dto.getFullName(), student.getFullName());
        assertEquals(dto.getUniversityId(), student.getUniversityId());
        assertEquals(dto.getInterests(), student.getInterests());
        assertEquals(dto.getSkills(), student.getSkills());
        assertEquals(dto.getPreferredLocation(), student.getPreferredLocation());
    }

    @Test
    void toDto_ShouldHandleNullFields() {
        Student student = new Student();
        student.setId(1L);
        student.setUsername("student1");
        student.setFullName(null);
        student.setUniversityId(null);
        student.setInterests(null);
        student.setSkills(null);
        student.setPreferredLocation(null);

        StudentDto dto = StudentMapper.toDto(student);

        assertNotNull(dto);
        assertNull(dto.getFullName());
        assertNull(dto.getUniversityId());
        assertNull(dto.getInterests());
        assertNull(dto.getSkills());
        assertNull(dto.getPreferredLocation());
    }

    @Test
    void toEntity_ShouldHandleNullFields() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("student1");

        StudentDto dto = new StudentDto();
        dto.setUserDto(userDto);
        dto.setFullName(null);
        dto.setUniversityId(null);
        dto.setInterests(null);
        dto.setSkills(null);
        dto.setPreferredLocation(null);

        Student student = StudentMapper.toEntity(dto);

        assertNotNull(student);
        assertNull(student.getFullName());
        assertNull(student.getUniversityId());
        assertNull(student.getInterests());
        assertNull(student.getSkills());
        assertNull(student.getPreferredLocation());
    }

}
