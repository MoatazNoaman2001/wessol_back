package com.wessol.app.features.presistant.entities.plan;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wessol.app.features.presistant.entities.representative.Representative;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.List;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Table(name = "Plan")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    private Integer duration;

    @OneToMany(mappedBy = "monthAttendancePay")
    @JsonManagedReference
    private List<Representative> representatives;
}
