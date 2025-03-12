package org.softwareretards.lobotomisedapp.dto.user;

import lombok.Data;

@Data
public class StudentDto {
    private UserDto userDto;

    private String fullName;
    private String universityId;
    private String interests;
    private String skills;
    private String preferredLocation;
}

