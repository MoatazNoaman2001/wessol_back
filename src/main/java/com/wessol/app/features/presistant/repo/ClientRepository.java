package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.clients.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.Optional;

@RepositoryRestController
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByPhoneNumber(String phoneNumber);
}
