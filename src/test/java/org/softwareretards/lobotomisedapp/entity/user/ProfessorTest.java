package org.softwareretards.lobotomisedapp.entity.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WavefrontProperties.Application.class)
class ProfessorTest {

    @InjectMocks
    private Professor professor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor(
                "prof_john",
                "password123",
                "John Smith",
                "Artificial Intelligence, Machine Learning"
        );
    }

    @AfterEach
    void tearDown() {
        professor = null;
    }

    @Test
    void testConstructor() {
        assertEquals("prof_john", professor.getUsername());
        assertEquals("password123", professor.getPassword());
        assertEquals(Role.PROFESSOR, professor.getRole());
        assertEquals("John Smith", professor.getProfessorName());
        assertEquals("Artificial Intelligence, Machine Learning", professor.getInterests());
    }

    @Test
    void testGettersAndSetters() {
        professor.setProfessorName("Jane Doe");
        assertEquals("Jane Doe",
                professor.getProfessorName());

        professor.setInterests("Data Science, Cloud Computing");
        assertEquals("Data Science, Cloud Computing",
                professor.getInterests());
    }

    @Test
    void testToString() {
        String expectedToString = "Professor{" +
                "id=" + professor.getId() +
                ", username='prof_john'" +
                ", role=PROFESSOR" +
                ", enabled=" + professor.isEnabled() +
                ", createdAt=" + professor.getCreatedAt() +
                ", updatedAt=" + professor.getUpdatedAt() +
                ", professionalName='John Smith'" +
                ", interests='Artificial Intelligence, Machine Learning'" +
                '}';

        assertEquals(expectedToString,
                professor.toString());
    }

    @Test
    void testInheritedFields() {
        assertEquals("prof_john",
                professor.getUsername());
        assertEquals("password123",
                professor.getPassword());
        assertEquals(Role.PROFESSOR,
                professor.getRole());
        assertTrue(professor.isEnabled());
        assertNotNull(professor.getCreatedAt());
        assertNotNull(professor.getUpdatedAt());
    }
}
