package org.softwareretards.lobotomisedapp.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.Role;

/**
 * Entity class representing a Professor.
 * <p>
 * Professors are responsible for supervising students during their traineeship,
 * reviewing their progress, and assisting in the evaluation process.
 * This class extends the {@link User} entity and is distinguished by the role PROFESSOR.
 * </p>
 */
@Entity
@Table(name = "professors")
@Getter
@Setter
@NoArgsConstructor
public class Professor extends User {

    @Column(name = "full_name")
    private String professorName;

    @Column(name = "interests")
    private String interests;

    public Professor(String username, String password, String professionalName, String interests) {
        super(username, password, Role.PROFESSOR);
        this.professorName = professionalName;
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", role=" + getRole() +
                ", enabled=" + isEnabled() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", professionalName='" + professorName + '\'' +
                ", interests='" + interests + '\'' +
                '}';
    }

}
