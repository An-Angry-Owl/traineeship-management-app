package org.softwareretards.lobotomisedapp.repository;

import org.softwareretards.lobotomisedapp.entity.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompany(Company company);
}
