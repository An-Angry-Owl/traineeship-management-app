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
        dto.setId(company.getId());
        dto.setUsername(company.getUsername());
        dto.setPassword(company.getPassword());
        dto.setRole(company.getRole());
        dto.setEnabled(company.isEnabled());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        dto.setCompanyName(company.getCompanyName());
        dto.setLocation(company.getLocation());
        return dto;
    }

    public static Company toEntity(CompanyDto dto) {
        if (dto == null) {
            return null;
        }
        Company company = new Company();
        company.setId(dto.getId());
        company.setUsername(dto.getUsername());
        company.setPassword(dto.getPassword());
        company.setRole(dto.getRole());
        company.setEnabled(dto.isEnabled());
        company.setCreatedAt(dto.getCreatedAt());
        company.setUpdatedAt(dto.getUpdatedAt());
        company.setCompanyName(dto.getCompanyName());
        company.setLocation(dto.getLocation());
        return company;
    }
}
