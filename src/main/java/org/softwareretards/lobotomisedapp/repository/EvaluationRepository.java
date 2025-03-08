package org.softwareretards.lobotomisedapp.repository;

import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByTraineeshipPositionId(Long positionId);
    List<Evaluation> findByFinalMark(FinalMark finalMark);
}