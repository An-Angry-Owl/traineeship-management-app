package org.softwareretards.lobotomisedapp.repository.traineeship;

import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompanyId(Long companyId);

    List<TraineeshipPosition> findByProfessor(Professor professor);

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.student IS NULL")
    List<TraineeshipPosition> findAvailablePositions();

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.company.id = :companyId AND t.student IS NULL")
    List<TraineeshipPosition> findAvailableByCompany(@Param("companyId") Long companyId);

    Optional<TraineeshipPosition> findByCompanyUsername(String username);
}
