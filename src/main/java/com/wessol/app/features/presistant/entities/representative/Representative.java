package com.wessol.app.features.presistant.entities.representative;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.plan.Plan;
import com.wessol.app.features.presistant.entities.products.Product;
import com.wessol.app.features.presistant.entities.wallet.Wallet;
import com.wessol.app.features.presistant.models.Pair;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="representativeTable")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nationalId")
public class Representative implements UserDetails , Principal {

    @Id
    private String NationalId;

    @Column(nullable = false, length = 255, name = "Name")
    @NotBlank(message = "Name Should be provided")
    private String name;

    @Column(name = "PhoneNumber", nullable = false, length = 16, unique = true)
    private String phoneNumber;

    @Embedded
    private Pair<Double, Double> location;

//    @Column(name = "_otp", nullable = true)
    @OneToMany(mappedBy = "representative")
    @JsonBackReference
    private List<OTP> otps;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    @Nullable
    private Plan monthAttendancePay;

    @OneToOne
    @JoinColumn(name = "wal_id")
    private Wallet wallet;

    @Column(nullable = true, name = "_plan_start_date")
    private LocalDateTime mothAttendancePayStartDate;

    @Column(name = "img")
    private String imageName;

    @OneToMany(mappedBy = "representative")
    private List<Product> products;
//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return otps.isEmpty()? "rjo34iu52iojrktwn89254thb" : otps.getLast().getOTP();
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
