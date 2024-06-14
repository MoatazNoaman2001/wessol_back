package com.wessol.app.features.presistant.models.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOTPModel {
    @NotBlank(message = "Must Enter OTP")
    @Size(max = 6, min = 6, message = "OTP Must be 6 digits")
    private String otp;
    private String phone;
}
