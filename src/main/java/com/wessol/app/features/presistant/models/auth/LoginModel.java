package com.wessol.app.features.presistant.models.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {
    private String phoneNumber;
    @NotBlank(message = "Must Enter OTP")
    @Size(max = 6, min = 6, message = "OTP Must be 6 digits")
    private String otp;
}
