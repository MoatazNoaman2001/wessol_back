package com.wessol.app.features.presentation.routes.auth;

import com.wessol.app.features.presistant.models.auth.RequestResponse;
import com.wessol.app.features.presistant.models.auth.SendOTPModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Register {


    @PostMapping
    ResponseEntity<RequestResponse> senOTP(SendOTPModel sendOTPModel){
        return ResponseEntity.ok(RequestResponse.builder().token("Still in dev").build());
    }

    @PostMapping
    ResponseEntity<RequestResponse> register(){
        return ResponseEntity.ok(RequestResponse.builder().token("Still in dev").build());
    }
}
