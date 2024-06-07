package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.payments.Method;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MethodRepository extends JpaRepository<Method, Long> {
    Optional<Method> findByMethod(String method);
}
