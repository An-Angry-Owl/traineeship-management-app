package org.softwareretards.lobotomisedapp.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwareretards.lobotomisedapp.entity.enums.Role;

/**
 * Entity class representing a Company.
 * <p>
 * This class extends the {@link User} class and is mapped to the "companies" table in the database.
 * It includes additional fields specific to company, such as companyName, location.
 * </p>
 */
@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor
public class Company extends User {

    @Column(name="company_name", nullable = false)
    private String companyName;

    @Column(name="location")
    private String location;

    public Company(String username, String password, String companyName, String location) {
        super(username, password, Role.COMPANY);
        this.companyName = companyName;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", role=" + getRole() +
                ", enabled=" + isEnabled() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
