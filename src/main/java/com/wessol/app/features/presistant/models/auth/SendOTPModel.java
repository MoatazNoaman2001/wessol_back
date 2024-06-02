package com.wessol.app.features.presistant.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendOTPModel {
    String nationalId, name, phoneNumber;
}
