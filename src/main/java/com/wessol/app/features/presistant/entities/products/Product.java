package com.wessol.app.features.presistant.entities.products;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wessol.app.features.presistant.entities.clients.Submission;
import com.wessol.app.features.presistant.entities.company.Company;
import com.wessol.app.features.presistant.entities.payments.Method;
import com.wessol.app.features.presistant.entities.place.ShippingPlaceE;
import com.wessol.app.features.presistant.entities.representative.Representative;
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

    @Column(name = "Cost", nullable = false)
    private long Cost;

    @Enumerated
    private DriveType driveType;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @JsonBackReference
    private ShippingPlaceE shippingPlace;

    @OneToOne
    @JoinColumn(name = "sub_id")
    private Submission sub;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "verifiedDate")
    private LocalDateTime verifiedDate;

    @Column(name = "receiveDate")
    private LocalDateTime ReceivedDate;

    @Enumerated
    @Column(name = "name")
    private ProductState productState;


    @ManyToOne
    @JoinColumn(name = "rep_id")
    private Representative representative;

}
