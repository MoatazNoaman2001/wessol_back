package com.wessol.app.features.presistant.entities.place;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.models.Pair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_ship_place")
public class ShippingPlaceE {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String place;

    @OneToMany(mappedBy = "shippingPlace")
    private List<Product> products;

}
