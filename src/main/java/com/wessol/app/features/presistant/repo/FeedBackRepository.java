package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.clients.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
}
