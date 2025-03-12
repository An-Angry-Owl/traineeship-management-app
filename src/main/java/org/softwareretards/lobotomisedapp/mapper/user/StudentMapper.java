package org.softwareretards.lobotomisedapp.mapper.user;

import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    private final UserMapper userMapper;

    public StudentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }
        StudentDto dto = new StudentDto();
        dto.setUserDto(userMapper.toDto(student)); // Convert User part
        dto.setFullName(student.getFullName());
        dto.setUniversityId(student.getUniversityId());
        dto.setInterests(student.getInterests());
        dto.setSkills(student.getSkills());
        dto.setPreferredLocation(student.getPreferredLocation());
        return dto;
    }

    public Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }
        Student student = new Student();
        student.setId(dto.getUserDto().getId());  // ID from UserDto
        student.setUsername(dto.getUserDto().getUsername());
        student.setRole(dto.getUserDto().getRole());
        student.setEnabled(dto.getUserDto().isEnabled());
        student.setCreatedAt(dto.getUserDto().getCreatedAt());
        student.setUpdatedAt(dto.getUserDto().getUpdatedAt());
        student.setFullName(dto.getFullName());
        student.setUniversityId(dto.getUniversityId());
        student.setInterests(dto.getInterests());
        student.setSkills(dto.getSkills());
        student.setPreferredLocation(dto.getPreferredLocation());
        return student;
    }
}