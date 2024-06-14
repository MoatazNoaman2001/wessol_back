package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.clients.Submission;
import com.wessol.app.features.presistant.entities.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.Optional;

@RepositoryRestController
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    Optional<Submission> findByProduct(Product product);
}
