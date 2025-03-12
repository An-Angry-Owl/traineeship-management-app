package org.softwareretards.lobotomisedapp.dto.user;
import lombok.Data;

@Data
public class ProfessorNDto {
    private UserNDto user;

    private String professorName;
    private String interests;
}
