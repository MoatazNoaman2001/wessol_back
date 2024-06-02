package com.wessol.app.features.presistant.entities.products;


import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String receiverPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "rep_id")
    private Representative representative;

    @Enumerated()
    @Column(name = "PayType")
    private PayType payType;

    @Column(name = "Cost", nullable = false)
    private long Cost;

    @Enumerated
    private DriveType driveType;

    @Enumerated
    private ShippingPlace shippingPlace;

}
