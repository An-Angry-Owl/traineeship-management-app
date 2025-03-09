package org.softwareretards.lobotomisedapp.repository.traineeship;

import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipApplication;
import org.softwareretards.lobotomisedapp.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipApplicationRepository extends JpaRepository<TraineeshipApplication, Long> {
    List<TraineeshipApplication> findByStudentId(Long studentId);
    List<TraineeshipApplication> findByStatus(ApplicationStatus status);
}