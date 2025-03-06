package org.softwareretards.lobotomisedapp.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.Role;

/**
 * Entity class representing a Committee member.
 * <p>
 * This class extends the {@link User} entity and is distinguished by the role COMMITTEE.
 * Committee members are responsible for assigning traineeship positions to students,
 * selecting available positions for students, assigning supervising professors,
 * and finalizing evaluations.
 * </p>
 */
@Entity
@Table(name = "committee")
@Getter
@Setter
@NoArgsConstructor
public class Committee extends User {

    @Column(name = "committee_name", nullable = false)
    private String committeeName;

    public Committee(String username, String password, String committeeName) {
        super(username, password, Role.COMMITTEE);
        this.committeeName = committeeName;
    }

    @Override
    public String toString() {
        return "Committee{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", role=" + getRole() +
                ", enabled=" + isEnabled() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

}
