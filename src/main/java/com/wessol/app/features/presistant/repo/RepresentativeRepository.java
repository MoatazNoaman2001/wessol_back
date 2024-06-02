package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.representative.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RepresentativeRepository extends JpaRepository<Representative, String> {
    Optional<Representative> findByPhoneNumber(String phoneNumber);
}
