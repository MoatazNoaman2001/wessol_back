package com.wessol.app.features.presentation.routes.auth;

import com.wessol.app.features.domain.services.AuthServiceImpl;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationUI {

    @Autowired
    AuthServiceImpl authService;

    @PostMapping("/sendOTP")
    ResponseEntity<SuccessResponse> sendOTP(@RequestBody SendOTPModel model){
        return ResponseEntity.status(CREATED).body(authService.sendOTP(model));
    }

    @PatchMapping("/sendOTP")
    ResponseEntity<SuccessResponse> regenerateOTP(@NotNull Authentication authentication){
        var rep = (Representative) authentication.getPrincipal();
        return ResponseEntity.status(CREATED).body(authService.regenerateOTP(rep));
    }


    @PostMapping("/verify")
    ResponseEntity<RequestResponse> verifyPhoneNumber(@RequestBody VerifyOTPModel verifyOTPModel){
        try{
            return ResponseEntity.ok(authService.verifyPhoneNumber(verifyOTPModel));
        }catch (RuntimeException e){
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    ResponseEntity<RequestResponse> login(@RequestBody LoginModel loginModel){
        return ResponseEntity.ok(authService.loginUSer(loginModel));
    }


}
