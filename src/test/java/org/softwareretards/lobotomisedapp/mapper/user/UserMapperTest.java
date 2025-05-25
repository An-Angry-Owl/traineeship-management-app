package org.softwareretards.lobotomisedapp.mapper.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertNull(UserMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenInputIsNull() {
        assertNull(UserMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.STUDENT);
        user.setEnabled(true);
        user.setCreatedAt(Timestamp.valueOf(now));
        user.setUpdatedAt(Timestamp.valueOf(now));

        UserDto dto = UserMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getRole(), dto.getRole());
        assertEquals(user.isEnabled(), dto.isEnabled());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("testuser");
        dto.setRole(Role.STUDENT);
        dto.setEnabled(true);
        dto.setCreatedAt(Timestamp.valueOf(now));
        dto.setUpdatedAt(Timestamp.valueOf(now));

        User user = UserMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getRole(), user.getRole());
        assertEquals(dto.isEnabled(), user.isEnabled());
        assertEquals(dto.getCreatedAt(), user.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), user.getUpdatedAt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "username"})
    void toDto_ShouldHandleVariousUsernameAndRoleValues(String value) {
        User user = new User();
        user.setUsername(value);
        user.setRole(Role.STUDENT);

        UserDto dto = UserMapper.toDto(user);

        assertEquals(value, dto.getUsername());
        assertEquals(Role.STUDENT, dto.getRole());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void toDto_ShouldHandleEnabledFlag(boolean enabled) {
        User user = new User();
        user.setEnabled(enabled);

        UserDto dto = UserMapper.toDto(user);

        assertEquals(enabled, dto.isEnabled());
    }

    @Test
    void toDto_ShouldHandleNullTimestamps() {
        User user = new User();
        user.setCreatedAt(null);
        user.setUpdatedAt(null);

        UserDto dto = UserMapper.toDto(user);

        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    void toEntity_ShouldHandleNullTimestamps() {
        UserDto dto = new UserDto();
        dto.setCreatedAt(null);
        dto.setUpdatedAt(null);

        User user = UserMapper.toEntity(dto);

        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }
}
