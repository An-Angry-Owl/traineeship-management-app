package org.softwareretards.lobotomisedapp.dto.userDtos;
import lombok.Data;

@Data
public class CommitteeDto {
    private UserDto userDto;

    private String committeeName;
}
