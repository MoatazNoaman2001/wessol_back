package com.wessol.app.features.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wessol.app.core.Config.Constants;
import com.wessol.app.features.presistant.entities.Role;
import com.wessol.app.features.presistant.entities.opt.OTP;
import com.wessol.app.features.presistant.entities.representative.Representative;
import com.wessol.app.features.presistant.models.auth.*;
import com.wessol.app.features.presistant.repo.OtpRepo;
import com.wessol.app.features.presistant.repo.RepresentativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RepresentativeRepository repo;
    private final PasswordEncoder encoder;
    private final OtpRepo otpRepo;
    private final JWT_Services jwt;

    private final AuthenticationManager authManger;

    private String otp = "";

    @Override
    public SuccessResponse sendOTP(SendOTPModel model) {
        System.out.println(model);
        Representative representative = Representative.builder()
                .name(model.getName())
                .NationalId(model.getNationalId())
                .phoneNumber(model.getPhoneNumber())
                .role(model.getIsAdmin().equalsIgnoreCase("true") ? Role.Admin: Role.Representative)
                .build();

        LetsBotModel letsBotModel = getLetsBootModel(representative);
        try {
            String requestBody = new ObjectMapper().writeValueAsString(letsBotModel);
            repo.save(representative);
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://letsbot.net/api/v1/message/send"))
                    .timeout(Duration.of(30, ChronoUnit.SECONDS))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Constants.LESTBOT_TOKEN)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                OTP otp = OTP.builder()
                        .OTP(encoder.encode(this.otp))
                        .representative(representative)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(30))
                        .build();
                otpRepo.save(otp);
                return SuccessResponse.builder().msg("Account created Please Verify {" + this.otp+ "}").build();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("request corrupted");

    }

    private LetsBotModel getLetsBootModel(Representative representative) {
        otp = getVerifyCode(6);
        return LetsBotModel.builder().phone(representative.getPhoneNumber()).body(
                Constants.LETSBOT_CONST_MSG.replaceAll("CODE_NUMEBR", otp)
        ).build();
    }

    private String getVerifyCode(int length) {
        String number = "0123456789";
        StringBuilder stringbuilder= new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int j = 0; j < length; j++) {
            int randomIndex = random.nextInt(number.length());
            stringbuilder.append(number.charAt(randomIndex));
        }
        return stringbuilder.toString();
    }

    @Override
    public RequestResponse verifyPhoneNumber(VerifyOTPModel model) {
        var auth = authManger.authenticate(
                new UsernamePasswordAuthenticationToken(
                        model.getPhone(),
                        model.getOtp()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((Representative) auth.getPrincipal());
        claims.put("national number" , user.getNationalId());
        var token = jwt.generateToken(claims , user);

        var otp = otpRepo.findByRepresentative(user).get().getLast();
        otp.setValidateAt(LocalDateTime.now());
        otpRepo.save(otp);

        return RequestResponse.builder().token(token).build();
    }

    @Override
    public RequestResponse loginUSer(LoginModel loginModel) {
        var auth = authManger.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginModel.getPhoneNumber(),
                        loginModel.getOtp()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((Representative) auth.getPrincipal());
        claims.put("national number" , user.getNationalId());
        var token = jwt.generateToken(claims , user);

        return RequestResponse.builder().token(token).build();
    }

}
