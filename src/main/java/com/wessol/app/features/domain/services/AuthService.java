package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.models.auth.*;

public interface AuthService {

    SuccessResponse sendOTP(SendOTPModel model);
    RequestResponse verifyPhoneNumber(VerifyOTPModel model);
    RequestResponse loginUSer(LoginModel loginModel);
}
