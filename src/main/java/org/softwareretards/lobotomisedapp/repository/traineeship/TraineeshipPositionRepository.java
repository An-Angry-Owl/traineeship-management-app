package org.softwareretards.lobotomisedapp.repository.traineeship;

import org.softwareretards.lobotomisedapp.entity.enums.TraineeshipStatus;
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

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.company.username = :username")
    List<TraineeshipPosition> findByCompanyUsername(@Param("username") String username); // Keep this

    Optional<TraineeshipPosition> findByIdAndCompanyUsername(
            @Param("positionId") Long positionId,
            @Param("username") String username
    );

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.professor.username = :professorUsername")
    List<TraineeshipPosition> findByProfessorUsername(String professorUsername);

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.student.preferredLocation = :studentPreferredLocation")
    List<TraineeshipPosition> findByStudentPreferredLocation(String studentPreferredLocation);

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.company.username = :companyUsername" +
            " AND t.status = :traineeshipStatus")
    List<TraineeshipPosition> findByCompanyUsernameAndStatus(String companyUsername, TraineeshipStatus traineeshipStatus);

    @Query("SELECT t FROM TraineeshipPosition t WHERE t.status = :traineeshipStatus")
    List<TraineeshipPosition> findAllByStatus(TraineeshipStatus traineeshipStatus);

    List<TraineeshipPosition> findAllByStudentIsNull();
}
