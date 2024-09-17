package com.wessol.app.features.presistant.entities.wallet;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "rep_wallet")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @OneToOne
    @JoinColumn(name = "rep_wal_id")
    private Representative representative;


    @Column(name = "saving", nullable = false)
    private Double Saving = 0.0;
}
