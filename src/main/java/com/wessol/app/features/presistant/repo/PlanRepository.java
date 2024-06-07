package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.Optional;

@RepositoryRestController
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Optional<Plan> findByTitle(String title);
}
