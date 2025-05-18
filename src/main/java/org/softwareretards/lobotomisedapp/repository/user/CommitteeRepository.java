package org.softwareretards.lobotomisedapp.repository.user;

import aj.org.objectweb.asm.commons.Remapper;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {
    List<Committee> findByCommitteeName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM users c WHERE c.username = ?1")
    Optional<Committee> findByUsername(String username);
}
