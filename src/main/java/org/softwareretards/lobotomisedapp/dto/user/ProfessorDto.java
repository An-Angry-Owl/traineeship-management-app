package org.softwareretards.lobotomisedapp.dto.user;
import lombok.Data;

@Data
public class ProfessorDto {
    private UserDto userDto;

    private String professorName;
    private String interests;

}
