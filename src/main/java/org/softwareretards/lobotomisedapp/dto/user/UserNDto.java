package org.softwareretards.lobotomisedapp.dto.user;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.enums.Role;

import java.sql.Timestamp;

@Data
public class UserNDto {
    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
