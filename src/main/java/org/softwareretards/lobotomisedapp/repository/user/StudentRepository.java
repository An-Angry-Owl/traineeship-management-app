package org.softwareretards.lobotomisedapp.repository.user;

import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUniversityId(String universityId);
    List<Student> findByPreferredLocation(String preferredLocation);
    Optional<Student> findByUsername(String username);
}