package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public static CompanyDto toDto(Company company) {
        if (company == null) {
            return null;
        }
        CompanyDto dto = new CompanyDto();
        dto.setUserDto(UserMapper.toDto(company)); // Convert User part
        dto.setCompanyName(company.getCompanyName());
        dto.setLocation(company.getLocation());
        return dto;
    }

    public static Company toEntity(CompanyDto dto) {
        if (dto == null) {
            return null;
        }
        Company company = new Company();
        company.setId(dto.getUserDto().getId());  // ID from UserDto
        company.setUsername(dto.getUserDto().getUsername());
        company.setRole(dto.getUserDto().getRole());
        company.setEnabled(dto.getUserDto().isEnabled());
        company.setCreatedAt(dto.getUserDto().getCreatedAt());
        company.setUpdatedAt(dto.getUserDto().getUpdatedAt());
        company.setCompanyName(dto.getCompanyName());
        company.setLocation(dto.getLocation());
        return company;
    }
}