package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;
import java.util.Optional;

@RepositoryRestController
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<List<Product>> findByRepresentative(Representative representative);
    Optional<List<Product>> findByReceiverPhoneNumber(String receiverPhoneNumber);
}
