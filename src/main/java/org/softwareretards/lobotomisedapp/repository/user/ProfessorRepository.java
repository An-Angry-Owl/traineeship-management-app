package org.softwareretards.lobotomisedapp.repository.user;

import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByInterestsContaining(String interests);

    @Query("SELECT p FROM Professor p ORDER BY (SELECT COUNT(t) FROM TraineeshipPosition t WHERE t.professor = p) ASC")
    List<Professor> findAllByOrderByWorkloadAsc();
}
