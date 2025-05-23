package org.softwareretards.lobotomisedapp.mapper.user;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.Committee;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommitteeMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenCommitteeIsNull() {
        assertNull(CommitteeMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(CommitteeMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Committee committee = new Committee();
        committee.setId(1L);
        committee.setUsername("committee1");
        committee.setRole(Role.valueOf("COMMITTEE"));
        committee.setEnabled(true);
        committee.setCreatedAt(Timestamp.valueOf(now));
        committee.setUpdatedAt(Timestamp.valueOf(now));
        committee.setCommitteeName("Admissions Committee");

        CommitteeDto dto = CommitteeMapper.toDto(committee);

        assertNotNull(dto);
        assertEquals(committee.getId(), dto.getUserDto().getId());
        assertEquals(committee.getUsername(), dto.getUserDto().getUsername());
        assertEquals(committee.getRole(), dto.getUserDto().getRole());
        assertEquals(committee.isEnabled(), dto.getUserDto().isEnabled());
        assertEquals(committee.getCreatedAt(), dto.getUserDto().getCreatedAt());
        assertEquals(committee.getUpdatedAt(), dto.getUserDto().getUpdatedAt());
        assertEquals(committee.getCommitteeName(), dto.getCommitteeName());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("committee1");
        userDto.setRole(Role.valueOf("COMMITTEE"));
        userDto.setEnabled(true);
        userDto.setCreatedAt(Timestamp.valueOf(now));
        userDto.setUpdatedAt(Timestamp.valueOf(now));

        CommitteeDto dto = new CommitteeDto();
        dto.setUserDto(userDto);
        dto.setCommitteeName("Admissions Committee");

        Committee committee = CommitteeMapper.toEntity(dto);

        assertNotNull(committee);
        assertEquals(dto.getUserDto().getId(), committee.getId());
        assertEquals(dto.getUserDto().getUsername(), committee.getUsername());
        assertEquals(dto.getUserDto().getRole(), committee.getRole());
        assertEquals(dto.getUserDto().isEnabled(), committee.isEnabled());
        assertEquals(dto.getUserDto().getCreatedAt(), committee.getCreatedAt());
        assertEquals(dto.getUserDto().getUpdatedAt(), committee.getUpdatedAt());
        assertEquals(dto.getCommitteeName(), committee.getCommitteeName());
    }

    @Test
    void toDto_ShouldHandleNullCommitteeName() {
        Committee committee = new Committee();
        committee.setId(1L);
        committee.setUsername("committee1");
        committee.setCommitteeName(null);

        CommitteeDto dto = CommitteeMapper.toDto(committee);

        assertNotNull(dto);
        assertNull(dto.getCommitteeName());
    }

    @Test
    void toEntity_ShouldHandleNullCommitteeName() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("committee1");

        CommitteeDto dto = new CommitteeDto();
        dto.setUserDto(userDto);
        dto.setCommitteeName(null);

        Committee committee = CommitteeMapper.toEntity(dto);

        assertNotNull(committee);
        assertNull(committee.getCommitteeName());
    }
}
