package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.products.ProductState;
import com.wessol.app.features.presistant.entities.representative.Representative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;
import java.util.Optional;

@RepositoryRestController
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<List<Product>> findByRepresentative(Representative representative);
    Optional<List<Product>> findByReceiverPhoneNumber(String receiverPhoneNumber);

    List<Product> findByProductState(ProductState productState);

//    Page<Product> getAllProductPaged(Pageable pageable);
}
