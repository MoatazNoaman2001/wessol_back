package com.wessol.app.features.presistant.repo;

import com.wessol.app.features.presistant.entities.opt.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepo extends JpaRepository<OTP, String> {
    Optional<OTP> findByOTP(String otp);
}
