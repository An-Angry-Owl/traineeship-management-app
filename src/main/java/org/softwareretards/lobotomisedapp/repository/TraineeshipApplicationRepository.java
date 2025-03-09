package org.softwareretards.lobotomisedapp.repository;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.TraineeshipApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipApplicationRepository extends JpaRepository<TraineeshipApplications, Long> {
    List<TraineeshipApplications> findByStudentId(Long studentId);
    List<TraineeshipApplications> findByStatus(String status);
}