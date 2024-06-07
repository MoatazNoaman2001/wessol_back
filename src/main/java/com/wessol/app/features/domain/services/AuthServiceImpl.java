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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RepresentativeRepository repo;
    private final PasswordEncoder encoder;
    private final OtpRepo otpRepo;
    private final JWT_Services jwt;
    private final AuthenticationManager authenticationManager;
    private String otp = "";

    @Override
    public SuccessResponse sendOTP(SendOTPModel model) {
        Representative representative = Representative.builder()
                .name(model.getName())
                .NationalId(model.getNationalId())
                .phoneNumber(model.getPhoneNumber())
                .role(Role.Representative)
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
                        .OTP(this.otp)
                        .representative(representative)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(30))
                        .build();
                otpRepo.save(otp);
                return SuccessResponse.builder().msg("Account created Please Verify").build();
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
        Representative representative= repo.findByPhoneNumber(model.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("Cant found wanted user"));
        OTP repOtp = otpRepo.findByRepresentative(representative)
                .orElseThrow(() -> new UsernameNotFoundException("No OTP Attached"));

//        System.out.println("otp is encoded: " +repOtp.getOTP()+ "\nreceived otp: " + encoder.encode(model.getOtp()));
        System.out.println(model.getOtp()+ " " +repOtp.getOTP());
        System.out.println("verify model" + model.toString());
        if (Objects.equals(model.getOtp(), repOtp.getOTP())) {
            representative.setOtp(model.getOtp());
            repOtp.setValidateAt(LocalDateTime.now());

            repo.save(representative);
            otpRepo.save(repOtp);

            String token = jwt.generateToken(representative);
            return RequestResponse.builder().token(token).build();
        }else{
            throw new RuntimeException("Wrong otp provided ");
        }
    }

    @Override
    public RequestResponse loginUSer(LoginModel loginModel) {
        Representative representative= repo.findByPhoneNumber(loginModel.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("Cant found wanted user"));
        OTP repOtp = otpRepo.findByRepresentative(representative)
                .orElseThrow(() -> new UsernameNotFoundException("No OTP Attached"));

        System.out.println("otp is encoded: " + repOtp.getOTP());
        System.out.println("verify model" + loginModel.toString());
        if (Objects.equals(encoder.encode(loginModel.getOtp()), repOtp.getOTP())) {

            String token = jwt.generateToken(representative);
            return RequestResponse.builder().token(token).build();
        }else{
            throw new RuntimeException("Wrong otp provided ");
        }
    }

}
