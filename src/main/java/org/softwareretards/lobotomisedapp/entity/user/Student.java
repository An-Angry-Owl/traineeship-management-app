package org.softwareretards.lobotomisedapp.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.Role;

/**
 * Entity class representing a Student.
 * <p>
 * This class extends the {@link User} class and is mapped to the "students" table in the database.
 * It includes additional fields specific to students, such as full name, university ID, interests, skills, and preferred location.
 * </p>
 */
@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor
public class Student extends User {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "university_id")
    private String universityId;

    @Column(name = "interests")
    private String interests;

    @Column(name = "skills")
    private String skills;

    @Column(name = "preferred_location")
    private String preferredLocation;

    public Student(String username,
                   String password,
                   String fullName,
                   String universityId,
                   String interests,
                   String skills,
                   String preferredLocation) {
        super(username, password, Role.STUDENT);
        this.fullName = fullName;
        this.universityId = universityId;
        this.interests = interests;
        this.skills = skills;
        this.preferredLocation = preferredLocation;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", role=" + getRole() +
                ", enabled=" + isEnabled() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", fullName='" + fullName + '\'' +
                ", universityId='" + universityId + '\'' +
                ", interests='" + interests + '\'' +
                ", skills='" + skills + '\'' +
                ", preferredLocation='" + preferredLocation + '\'' +
                '}';
    }
}
