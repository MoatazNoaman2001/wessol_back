package com.wessol.app.features.presistant.entities.products;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wessol.app.features.presistant.entities.clients.Submission;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.Pair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "rec_name")
    private String receiverName;

    @Column(nullable = false, name = "rec_phone")
    private String receiverPhoneNumber;

    @ManyToOne()
    @JoinColumn(name = "pay_id")
    private Method payType;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "shipping_cost")
    private double shippingCost = 0;

    @Column(name = "Cost", nullable = false)
    private double Cost;

    @Column(name = "rep_earn" , nullable = false)
    private float representativeEarnings = 0.1f;

    @Column(name = "rec_pay")
    private double receivedPay = 0;

    @Enumerated
    private DriveType driveType;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private ShippingPlaceE shippingPlace;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_id", referencedColumnName = "id")
    private Submission sub;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "verifiedDate")
    private LocalDateTime verifiedDate;

    @Column(name = "receiveDate")
    private LocalDateTime ReceivedDate;

    @Column(name = "returnDate")
    private LocalDateTime returnDate;

    @Column(name = "cancelDate")
    private LocalDateTime cancelDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ProductState productState;

    @ManyToOne
    @JoinColumn(name = "rep_id")
    private Representative representative;

    public void confirmReceive(){
        ReceivedDate = LocalDateTime.now();
        isPaid = true;
        productState = ProductState.DELIVERED;
    }

    public void returnProduct(){
        returnDate = LocalDateTime.now();
        productState = ProductState.Returned;
    }
    public void cancelProduct(){
        cancelDate = LocalDateTime.now();
        productState = ProductState.Canceled;
    }
}
