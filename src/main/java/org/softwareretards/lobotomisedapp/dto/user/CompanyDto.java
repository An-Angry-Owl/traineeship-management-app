package org.softwareretards.lobotomisedapp.dto.user;
import lombok.Data;

@Data
public class CompanyDto {
    private UserDto userDto;

    private String companyName;
    private String location;
}

