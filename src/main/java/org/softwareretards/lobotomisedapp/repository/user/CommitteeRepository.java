package org.softwareretards.lobotomisedapp.repository.user;

import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {
    List<Committee> findByCommitteeName(String name);
    List<Committee> findByUsername(String username);
}
