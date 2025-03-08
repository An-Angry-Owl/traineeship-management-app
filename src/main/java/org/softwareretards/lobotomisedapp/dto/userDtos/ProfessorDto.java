package org.softwareretards.lobotomisedapp.dto.userDtos;
import lombok.Data;

@Data
public class ProfessorDto {
    private UserDto user;

    private String professorName;
    private String interests;
}
