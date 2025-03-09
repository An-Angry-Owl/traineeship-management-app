package org.softwareretards.lobotomisedapp.repository.user;

import org.softwareretards.lobotomisedapp.entity.user.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCompanyName(String name);
    List<Company> findByLocation(String location);
}
