package org.softwareretards.lobotomisedapp.repository.traineeship;

import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeshipPositionRepository extends JpaRepository<TraineeshipPosition, Long> {
    List<TraineeshipPosition> findByCompany(Long companyID);
    List<TraineeshipPosition> findByCompanyAndAvailability(Long companyId, Boolean availability);
}
