package org.softwareretards.lobotomisedapp.entity.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(
                "john_doe",
                "password123",
                "John Doe",
                "123456789",
                "Programming, AI, Machine Learning",
                "Java, Python, SQL",
                "New York"
        );
    }

    @AfterEach
    void tearDown() {
        student = null;
    }

    @Test
    void testConstructor() {
        assertEquals("john_doe", student.getUsername());
        assertEquals("password123", student.getPassword());
        assertEquals(Role.STUDENT, student.getRole());
        assertEquals("John Doe", student.getFullName());
        assertEquals("123456789", student.getUniversityId());
        assertEquals("Programming, AI, Machine Learning", student.getInterests());
        assertEquals("Java, Python, SQL", student.getSkills());
        assertEquals("New York", student.getPreferredLocation());
    }

    @Test
    void testGettersAndSetters() {
        student.setFullName("Jane Doe");
        assertEquals("Jane Doe", student.getFullName());

        student.setUniversityId("987654321");
        assertEquals("987654321", student.getUniversityId());

        student.setInterests("Data Science, Cloud Computing");
        assertEquals("Data Science, Cloud Computing", student.getInterests());

        student.setSkills("JavaScript, React, AWS");
        assertEquals("JavaScript, React, AWS", student.getSkills());

        student.setPreferredLocation("San Francisco");
        assertEquals("San Francisco", student.getPreferredLocation());
    }

    @Test
    void testToString() {
        String expectedToString = "Student{" +
                "id=" + student.getId() +
                ", username='john_doe'" +
                ", role=STUDENT" +
                ", enabled=" + student.isEnabled() +
                ", createdAt=" + student.getCreatedAt() +
                ", updatedAt=" + student.getUpdatedAt() +
                ", fullName='John Doe'" +
                ", universityId='123456789'" +
                ", interests='Programming, AI, Machine Learning'" +
                ", skills='Java, Python, SQL'" +
                ", preferredLocation='New York'" +
                '}';

        assertEquals(expectedToString, student.toString());
    }

    @Test
    void testInheritedFields() {
        assertEquals("john_doe", student.getUsername());
        assertEquals("password123", student.getPassword());
        assertEquals(Role.STUDENT, student.getRole());
        assertTrue(student.isEnabled());
        assertNotNull(student.getCreatedAt());
        assertNotNull(student.getUpdatedAt());
    }
}