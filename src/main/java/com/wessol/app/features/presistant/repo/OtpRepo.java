package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.representative.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface OtpRepo extends JpaRepository<OTP, Long> {
    Optional<OTP> findByOTP(String OTP);
    Optional<List<OTP>> findByRepresentative(Representative representative);
}
