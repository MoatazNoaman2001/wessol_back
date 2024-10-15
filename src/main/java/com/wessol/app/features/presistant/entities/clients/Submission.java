package com.wessol.app.features.presistant.entities.clients;


import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.models.Pair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Embedded
    @Column(name = "location", nullable = false)
    private Pair<Double , Double> location;

    @Column(name = "image", nullable = false)
    private String imgName;

    @Column(name = "is_postponed")
    private boolean isPostponed = false;

    @Column
    private String cause;

    @OneToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "id")
    private Product product;

    @Enumerated
    @Column(name = "authority")
    private Role role = Role.User;


}
