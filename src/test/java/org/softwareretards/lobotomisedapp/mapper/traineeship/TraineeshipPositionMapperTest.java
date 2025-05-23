package org.softwareretards.lobotomisedapp.mapper.traineeship;

import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CompanyDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.softwareretards.lobotomisedapp.entity.user.Student;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraineeshipPositionMapperTest {

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(TraineeshipPositionMapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(TraineeshipPositionMapper.toEntity(null));
    }

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        TraineeshipPosition entity = new TraineeshipPosition();
        entity.setId(1L);
        entity.setStartDate(LocalDate.of(2023, 1, 1));
        entity.setEndDate(LocalDate.of(2023, 6, 30));
        entity.setDescription("Software Development Internship");
        entity.setRequiredSkills(String.valueOf(List.of("Java", "Spring")));
        entity.setTopics(String.valueOf(List.of("Web Development", "Microservices")));
        entity.setStatus(TraineeshipStatus.valueOf("OPEN"));

        Company company = new Company();
        company.setId(1L);
        entity.setCompany(company);

        Student student = new Student();
        student.setId(2L);
        entity.setStudent(student);

        Professor professor = new Professor();
        professor.setId(3L);
        entity.setProfessor(professor);

        LogbookEntry logbookEntry = new LogbookEntry();
        logbookEntry.setId(4L);
        entity.setLogbookEntries(List.of(logbookEntry));

        TraineeshipPositionDto dto = TraineeshipPositionMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getStartDate(), dto.getStartDate());
        assertEquals(entity.getEndDate(), dto.getEndDate());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getRequiredSkills(), dto.getRequiredSkills());
        assertEquals(entity.getTopics(), dto.getTopics());
        assertEquals(entity.getStatus(), dto.getStatus());
        assertNotNull(dto.getCompany());
        assertNotNull(dto.getStudent());
        assertNotNull(dto.getProfessor());
        assertEquals(1, dto.getLogbookEntries().size());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUserDto(userDto);
        companyDto.getUserDto().setId(1L);

        StudentDto studentDto = new StudentDto();
        studentDto.setUserDto(userDto);
        studentDto.getUserDto().setId(2L);

        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setUserDto(userDto);
        professorDto.getUserDto().setId(3L);

        TraineeshipPositionDto dto = new TraineeshipPositionDto();
        dto.setId(1L);
        dto.setStartDate(LocalDate.of(2023, 1, 1));
        dto.setEndDate(LocalDate.of(2023, 6, 30));
        dto.setDescription("Software Development Internship");
        dto.setRequiredSkills(String.valueOf(List.of("Java", "Spring")));
        dto.setTopics(String.valueOf(List.of("Web Development", "Microservices")));
        dto.setStatus(TraineeshipStatus.valueOf("OPEN"));
        dto.setCompany(companyDto);
        dto.setStudent(studentDto);
        dto.setProfessor(professorDto);

        LogbookEntryDto logbookEntryDto = new LogbookEntryDto();
        logbookEntryDto.setId(4L);
        dto.setLogbookEntries(List.of(logbookEntryDto));

        TraineeshipPosition entity = TraineeshipPositionMapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getStartDate(), entity.getStartDate());
        assertEquals(dto.getEndDate(), entity.getEndDate());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getRequiredSkills(), entity.getRequiredSkills());
        assertEquals(dto.getTopics(), entity.getTopics());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertNotNull(entity.getCompany());
        assertNotNull(entity.getStudent());
        assertNotNull(entity.getProfessor());
        assertEquals(1, entity.getLogbookEntries().size());
        assertEquals(logbookEntryDto.getId(), entity.getLogbookEntries().get(0).getId());
    }

    @Test
    void toDto_ShouldHandleNullCollections() {
        TraineeshipPosition entity = new TraineeshipPosition();
        entity.setRequiredSkills(null);
        entity.setTopics(null);
        entity.setLogbookEntries(null);

        TraineeshipPositionDto dto = TraineeshipPositionMapper.toDto(entity);

        assertNotNull(dto);
        assertNull(dto.getRequiredSkills());
        assertNull(dto.getTopics());
        assertNull(dto.getLogbookEntries());
    }

    @Test
    void toEntity_ShouldHandleNullCollections() {
        TraineeshipPositionDto dto = new TraineeshipPositionDto();
        dto.setRequiredSkills(null);
        dto.setTopics(null);
        dto.setLogbookEntries(null);

        TraineeshipPosition entity = TraineeshipPositionMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getRequiredSkills());
        assertNull(entity.getTopics());
        assertNotNull(entity.getLogbookEntries());
        assertTrue(entity.getLogbookEntries().isEmpty());
    }
}
