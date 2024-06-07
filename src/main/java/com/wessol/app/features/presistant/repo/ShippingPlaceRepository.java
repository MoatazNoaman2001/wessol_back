package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingPlaceRepository extends JpaRepository<ShippingPlaceE, Long> {
    Optional<ShippingPlaceE> findByPlace(String place);
}
