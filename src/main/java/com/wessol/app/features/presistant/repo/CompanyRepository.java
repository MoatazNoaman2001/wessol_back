package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;
import java.util.Optional;

@RepositoryRestController
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String Name);
//    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.products WHERE (:role = 'admin' OR c.products IS EMPTY)")
//    List<Company> findCompaniesByRole(@Param("role") String role);
}
