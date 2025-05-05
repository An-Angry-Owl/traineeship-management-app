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
        dto.setId(professor.getId());
        dto.setUsername(professor.getUsername());
        dto.setPassword(professor.getPassword());
        dto.setRole(professor.getRole());
        dto.setEnabled(professor.isEnabled());
        dto.setCreatedAt(professor.getCreatedAt());
        dto.setUpdatedAt(professor.getUpdatedAt());
        dto.setProfessorName(professor.getProfessorName());
        dto.setInterests(professor.getInterests());
        return dto;
    }

    public static Professor toEntity(ProfessorDto dto) {
        if (dto == null) {
            return null;
        }
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setUsername(dto.getUsername());
        professor.setPassword(dto.getPassword());
        professor.setRole(dto.getRole());
        professor.setEnabled(dto.isEnabled());
        professor.setCreatedAt(dto.getCreatedAt());
        professor.setUpdatedAt(dto.getUpdatedAt());
        professor.setProfessorName(dto.getProfessorName());
        professor.setInterests(dto.getInterests());
        return professor;
    }
}
