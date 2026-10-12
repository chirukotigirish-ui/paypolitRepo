package com.paypilot.controller;

import com.paypilot.dto.ForgotPasswordRequest;
import com.paypilot.dto.ForgotPasswordResponse;
import com.paypilot.dto.LoginRequest;
import com.paypilot.dto.LoginResponse;
import com.paypilot.dto.RegisterRequest;
import com.paypilot.dto.RegisterResponse;
import com.paypilot.dto.ResetPasswordRequest;
import com.paypilot.dto.ResetPasswordResponse;
import com.paypilot.dto.VerifyOtpRequest;
import com.paypilot.dto.VerifyOtpResponse;
import com.paypilot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        VerifyOtpResponse response =
                authService.verifyOtp(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        ForgotPasswordResponse response =
                authService.forgotPassword(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        ResetPasswordResponse response =
                authService.resetPassword(request);

        return ResponseEntity.ok(response);
    }
}