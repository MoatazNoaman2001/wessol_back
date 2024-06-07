package com.wessol.app.features.presistant.entities.plan;

import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Table(name = "Plan")
@Entity
public class Plan {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String title;

    @Column(name = "MonthAttendancePay", nullable = false)
    Double AttendancePay;

    @ElementCollection
    @Column(name = "benefits")
    private List<String> prons;

    @OneToMany(mappedBy = "monthAttendancePay")
    private List<Representative> representatives;
}
