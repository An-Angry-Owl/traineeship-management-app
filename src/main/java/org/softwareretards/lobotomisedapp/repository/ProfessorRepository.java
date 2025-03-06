package org.softwareretards.lobotomisedapp.repository;

import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByInterestsContaining(String interests);
}
