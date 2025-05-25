package org.softwareretards.lobotomisedapp.mapper.user;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.Company;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CompanyMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenCompanyIsNull() {
        assertNull(CompanyMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(CompanyMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Company company = new Company();
        company.setId(1L);
        company.setUsername("company1");
        company.setRole(Role.valueOf("COMPANY"));
        company.setEnabled(true);
        company.setCreatedAt(Timestamp.valueOf(now));
        company.setUpdatedAt(Timestamp.valueOf(now));
        company.setCompanyName("Tech Corp");
        company.setLocation("Silicon Valley");

        CompanyDto dto = CompanyMapper.toDto(company);

        assertNotNull(dto);
        assertEquals(company.getId(), dto.getUserDto().getId());
        assertEquals(company.getUsername(), dto.getUserDto().getUsername());
        assertEquals(company.getRole(), dto.getUserDto().getRole());
        assertEquals(company.isEnabled(), dto.getUserDto().isEnabled());
        assertEquals(company.getCreatedAt(), dto.getUserDto().getCreatedAt());
        assertEquals(company.getUpdatedAt(), dto.getUserDto().getUpdatedAt());
        assertEquals(company.getCompanyName(), dto.getCompanyName());
        assertEquals(company.getLocation(), dto.getLocation());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("company1");
        userDto.setRole(Role.valueOf("COMPANY"));
        userDto.setEnabled(true);
        userDto.setCreatedAt(Timestamp.valueOf(now));
        userDto.setUpdatedAt(Timestamp.valueOf(now));

        CompanyDto dto = new CompanyDto();
        dto.setUserDto(userDto);
        dto.setCompanyName("Tech Corp");
        dto.setLocation("Silicon Valley");

        Company company = CompanyMapper.toEntity(dto);

        assertNotNull(company);
        assertEquals(dto.getUserDto().getId(), company.getId());
        assertEquals(dto.getUserDto().getUsername(), company.getUsername());
        assertEquals(dto.getUserDto().getRole(), company.getRole());
        assertEquals(dto.getUserDto().isEnabled(), company.isEnabled());
        assertEquals(dto.getUserDto().getCreatedAt(), company.getCreatedAt());
        assertEquals(dto.getUserDto().getUpdatedAt(), company.getUpdatedAt());
        assertEquals(dto.getCompanyName(), company.getCompanyName());
        assertEquals(dto.getLocation(), company.getLocation());
    }

    @Test
    void toDto_ShouldHandleNullFields() {
        Company company = new Company();
        company.setId(1L);
        company.setUsername("company1");
        company.setCompanyName(null);
        company.setLocation(null);

        CompanyDto dto = CompanyMapper.toDto(company);

        assertNotNull(dto);
        assertNull(dto.getCompanyName());
        assertNull(dto.getLocation());
    }

    @Test
    void toEntity_ShouldHandleNullFields() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("company1");

        CompanyDto dto = new CompanyDto();
        dto.setUserDto(userDto);
        dto.setCompanyName(null);
        dto.setLocation(null);

        Company company = CompanyMapper.toEntity(dto);

        assertNotNull(company);
        assertNull(company.getCompanyName());
        assertNull(company.getLocation());
    }
}
