package org.softwareretards.lobotomisedapp.dto.userDtos;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private boolean enabled;
    private String createdAt;
    private String updatedAt;
}

