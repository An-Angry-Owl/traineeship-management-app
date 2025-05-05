package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public static StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setUsername(student.getUsername());
        dto.setPassword(student.getPassword());
        dto.setRole(student.getRole());
        dto.setEnabled(student.isEnabled());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        dto.setFullName(student.getFullName());
        dto.setUniversityId(student.getUniversityId());
        dto.setInterests(student.getInterests());
        dto.setSkills(student.getSkills());
        dto.setPreferredLocation(student.getPreferredLocation());
        return dto;
    }

    public static Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }
        Student student = new Student();
        student.setId(dto.getId());
        student.setUsername(dto.getUsername());
        student.setPassword(dto.getPassword());
        student.setRole(dto.getRole());
        student.setEnabled(dto.isEnabled());
        student.setCreatedAt(dto.getCreatedAt());
        student.setUpdatedAt(dto.getUpdatedAt());
        student.setFullName(dto.getFullName());
        student.setUniversityId(dto.getUniversityId());
        student.setInterests(dto.getInterests());
        student.setSkills(dto.getSkills());
        student.setPreferredLocation(dto.getPreferredLocation());
        return student;
    }
}
