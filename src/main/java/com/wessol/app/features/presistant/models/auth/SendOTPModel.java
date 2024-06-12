package com.wessol.app.features.presistant.models.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOTPModel {

    @NotBlank(message = "must provide national Id")
    private String nationalId;
    private String name;
    @Size(min = 8, message = "must phone number contain country code")
    @NotBlank(message = "must enter phone Number")
    private String phoneNumber;
    private String isAdmin = "";
}
