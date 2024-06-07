package com.wessol.app.features.presistant.entities.company;

import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Must Enter Method Name")
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private List<Product> products;


}
