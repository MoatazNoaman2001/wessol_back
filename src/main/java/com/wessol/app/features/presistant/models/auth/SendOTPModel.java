package com.wessol.app.features.presistant.models.auth;

import jakarta.validation.constraints.NotBlank;
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
    private String phoneNumber;
}
