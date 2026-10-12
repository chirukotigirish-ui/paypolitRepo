package com.paypilot.service;

import com.paypilot.dto.ForgotPasswordRequest;
import com.paypilot.dto.ForgotPasswordResponse;
import com.paypilot.dto.ResetPasswordRequest;
import com.paypilot.dto.ResetPasswordResponse;
import com.paypilot.entity.AppUser;
import com.paypilot.repository.AppUserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthServicePasswordTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    private AppUser user;

    @BeforeEach
    void setUp() {

        user = appUserRepository
                .findByUsername("testuser123")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Test user testuser123 was not found in database"
                        ));

        if (!"APPROVED".equals(user.getStatus())) {
            throw new IllegalStateException(
                    "Test user testuser123 must have APPROVED status"
            );
        }
    }

    @Test
    void forgotPasswordShouldGeneratePasswordResetOtp() {

        ForgotPasswordRequest request =
                new ForgotPasswordRequest();

        request.setEmail(user.getEmail());

        ForgotPasswordResponse response =
                authService.forgotPassword(request);

        assertNotNull(response);

        assertEquals(
                "Password reset OTP has been sent to your registered email.",
                response.getMessage()
        );
    }

    @Test
    void resetPasswordShouldChangePasswordAfterOtpVerification() {

        String otp =
                otpService.generateOtp(
                        user,
                        "PASSWORD_RESET"
                );

        assertNotNull(otp);

        assertEquals(
                6,
                otp.length()
        );

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setUsername(user.getUsername());
        request.setOtp(otp);
        request.setNewPassword("NewPass123!");

        ResetPasswordResponse response =
                authService.resetPassword(request);

        assertNotNull(response);

        assertEquals(
                user.getUsername(),
                response.getUsername()
        );

        assertEquals(
                "Password reset successfully",
                response.getMessage()
        );

        AppUser updatedUser =
                appUserRepository
                        .findByUsername(user.getUsername())
                        .orElseThrow();

        boolean passwordMatches =
                passwordEncoder.matches(
                        "NewPass123!",
                        updatedUser.getPasswordHash()
                );

        assertTrue(passwordMatches);
    }
}