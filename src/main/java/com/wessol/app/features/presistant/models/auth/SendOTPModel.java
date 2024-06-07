package com.wessol.app.features.presistant.models.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOTPModel {
    private String nationalId;
    private String name;
    private String phoneNumber;
}
