package org.softwareretards.lobotomisedapp.entity.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompanyTest {

    @InjectMocks
    private Company company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        company = new Company(
                "company_user",
                "password123",
                "Tech Corp",
                "San Francisco"
        );
    }

    @Test
    void testConstructor() {
        assertEquals("company_user", company.getUsername());
        assertEquals("password123", company.getPassword());
        assertEquals(Role.STUDENT, company.getRole());
        assertEquals("Tech Corp", company.getCompanyName());
        assertEquals("San Francisco", company.getLocation());
    }

    @Test
    void testGettersAndSetters() {
        // Test getters and setters for all fields
        company.setCompanyName("Innovate Inc");
        assertEquals("Innovate Inc", company.getCompanyName());

        company.setLocation("New York");
        assertEquals("New York", company.getLocation());
    }

    @Test
    void testToString() {
        String expectedToString = "Student{" +
                "id=" + company.getId() +
                ", username='company_user'" +
                ", role=STUDENT" +
                ", enabled=" + company.isEnabled() +
                ", createdAt=" + company.getCreatedAt() +
                ", updatedAt=" + company.getUpdatedAt() +
                ", companyName='Tech Corp'" +
                ", location='San Francisco'" +
                '}';

        assertEquals(expectedToString, company.toString());
    }

    @Test
    void testInheritedFields() {
        assertEquals("company_user", company.getUsername());
        assertEquals("password123", company.getPassword());
        assertEquals(Role.STUDENT, company.getRole());
        assertTrue(company.isEnabled());
        assertNotNull(company.getCreatedAt());
        assertNotNull(company.getUpdatedAt());
    }
}
