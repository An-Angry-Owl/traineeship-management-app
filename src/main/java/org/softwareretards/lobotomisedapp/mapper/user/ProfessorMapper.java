package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public static ProfessorDto toDto(Professor professor) {
        if (professor == null) {
            return null;
        }
        ProfessorDto dto = new ProfessorDto();
        dto.setUserDto(UserMapper.toDto(professor)); // Convert User part
        dto.setProfessorName(professor.getProfessorName());
        dto.setInterests(professor.getInterests());
        return dto;
    }

    public static Professor toEntity(ProfessorDto dto) {
        if (dto == null) {
            return null;
        }
        Professor professor = new Professor();
        professor.setId(dto.getUserDto().getId());  // ID from UserDto
        professor.setUsername(dto.getUserDto().getUsername());
        professor.setRole(dto.getUserDto().getRole());
        professor.setEnabled(dto.getUserDto().isEnabled());
        professor.setCreatedAt(dto.getUserDto().getCreatedAt());
        professor.setUpdatedAt(dto.getUserDto().getUpdatedAt());
        professor.setProfessorName(dto.getProfessorName());
        professor.setInterests(dto.getInterests());
        return professor;
    }
}