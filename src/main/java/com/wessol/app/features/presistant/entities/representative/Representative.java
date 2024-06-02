package com.wessol.app.features.presistant.entities.representative;

import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.plan.Plan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="representativeTable")
public class Representative implements UserDetails {

    @Id
    private String NationalId;

    @Column(nullable = false, length = 255, name = "Name")
    @NotBlank(message = "Name Should be provided")
    private String name;

    @Column(name = "PhoneNumber", nullable = false, length = 16)
    private String phoneNumber;

    @OneToMany(mappedBy = "representative")
    private List<OTP> otps;

    @Enumerated
    @Column(name = "authority")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan monthAttendancePay;
//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return otps.getLast().getOTP();
    }

    @Override
    public String getUsername() {
        return NationalId;
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
