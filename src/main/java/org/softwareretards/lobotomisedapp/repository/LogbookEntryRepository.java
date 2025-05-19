package org.softwareretards.lobotomisedapp.repository;

import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.softwareretards.lobotomisedapp.entity.traineeship.TraineeshipPosition;
import org.softwareretards.lobotomisedapp.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogbookEntryRepository extends JpaRepository<LogbookEntry, Long> {
    List<LogbookEntry> findByStudentId(Long studentId);
    List<LogbookEntry> findByPositionId(Long positionId);

    @Query("SELECT le FROM LogbookEntry le WHERE le.student = ?1 AND le.position = ?2")
    List<LogbookEntry> findByStudentAndPosition(Student student, TraineeshipPosition position);
}