package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.Optional;

@RepositoryRestController
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String Name);
}
