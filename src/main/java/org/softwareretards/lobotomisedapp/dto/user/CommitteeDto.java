package org.softwareretards.lobotomisedapp.dto.user;
import lombok.Data;

@Data
public class CommitteeDto {
    private UserDto userDto;

    private String committeeName;
}
