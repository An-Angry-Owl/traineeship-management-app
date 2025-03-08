package org.softwareretards.lobotomisedapp.repository;

import org.softwareretards.lobotomisedapp.entity.LogbookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogbookEntryRepository extends JpaRepository<LogbookEntry, Long> {
    List<LogbookEntry> findByStudentId(Long studentId);
    List<LogbookEntry> findByPositionId(Long positionId);
}