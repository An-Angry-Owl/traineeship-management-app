package org.softwareretards.lobotomisedapp.dto.user;
import lombok.Data;

@Data
public class CompanyNDto {
    private UserNDto userDto;

    private String companyName;
    private String location;
}

