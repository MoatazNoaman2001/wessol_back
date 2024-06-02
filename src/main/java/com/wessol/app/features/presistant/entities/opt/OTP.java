package com.wessol.app.features.presistant.entities.opt;


import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_opt")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String OTP;

    private LocalDateTime createdAt;
    private LocalDateTime validateAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "_rep_id", nullable = false)
    private Representative representative;
}
