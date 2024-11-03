package com.wessol.app.features.domain.services;

import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface AuthService {

    SuccessResponse sendOTP(SendOTPModel model);
    SuccessResponse regenerateOTP(Representative rep);
    RequestResponse verifyPhoneNumber(VerifyOTPModel model);
    RequestResponse loginUSer(LoginModel loginModel) throws IOException;
}
