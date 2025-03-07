package com.wessol.app.features.presistant.entities.payments;


import com.fasterxml.jackson.annotation.*;
import com.wessol.app.features.presistant.entities.products.Product;
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
@Table(name = "_payment_methods")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, name = "name")
    private String method;

    @Column(name = "img_name")
    private String imageName;


    @OneToMany(mappedBy = "payType")
    @JsonManagedReference
    private List<Product> products;
}
