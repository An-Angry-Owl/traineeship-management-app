package org.softwareretards.lobotomisedapp.repository;

import lombok.Data;
import org.softwareretards.lobotomisedapp.entity.TraineeshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipApplicationRepository extends JpaRepository<TraineeshipApplication, Long> {
    List<TraineeshipApplication> findByStudentId(Long studentId);
    List<TraineeshipApplication> findByStatus(String status);
}