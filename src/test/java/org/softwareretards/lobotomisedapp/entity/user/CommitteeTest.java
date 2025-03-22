package org.softwareretards.lobotomisedapp.entity.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.softwareretards.lobotomisedapp.entity.enums.Role;

import static org.junit.jupiter.api.Assertions.*;

class CommitteeTest {

    @InjectMocks
    private Committee committee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        committee = new Committee(
                "committee_user",
                "password123",
                "Traineeship Committee"
        );
    }

    @Test
    void testConstructor() {
        assertEquals("committee_user", committee.getUsername());
        assertEquals("password123", committee.getPassword());
        assertEquals(Role.COMMITTEE, committee.getRole());
        assertEquals("Traineeship Committee", committee.getCommitteeName());
    }

    @Test
    void testGettersAndSetters() {
        committee.setCommitteeName("New Committee Name");
        assertEquals("New Committee Name", committee.getCommitteeName());
    }

    @Test
    void testToString() {
        String expectedToString = "Committee{" +
                "id=" + committee.getId() +
                ", username='committee_user'" +
                ", role=COMMITTEE" +
                ", enabled=" + committee.isEnabled() +
                ", createdAt=" + committee.getCreatedAt() +
                ", updatedAt=" + committee.getUpdatedAt() +
                '}';

        assertEquals(expectedToString, committee.toString());
    }

    @Test
    void testInheritedFields() {
        assertEquals("committee_user", committee.getUsername());
        assertEquals("password123", committee.getPassword());
        assertEquals(Role.COMMITTEE, committee.getRole());
        assertTrue(committee.isEnabled());
        assertNotNull(committee.getCreatedAt());
        assertNotNull(committee.getUpdatedAt());
    }
}
