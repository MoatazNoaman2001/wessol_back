package com.wessol.app.features.presistant.entities.clients;


import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.models.Pair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clientTable")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "first_n", nullable = false)
    String firstName;
    @Column(name = "last_n", nullable = false)
    String lastName;

    @Column(name = "PhoneNumber", nullable = false)
    String phoneNumber;

    @Column(name = "message", length = 2048)
    String Message;

    @Embedded
    @Column(name = "location", nullable = false)
    private Pair<String , String> location;

    @Column(name = "image", nullable = false)
    private String imgName;

    @Column(name = "is_postponed")
    private boolean isPostponed = false;

    @Column
    private String cause;

    @Enumerated
    @Column(name = "authority")
    private Role role = Role.User;


}
