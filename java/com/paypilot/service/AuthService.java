package com.paypilot.service;

import com.paypilot.dto.AccessRequestDecisionRequest;
import com.paypilot.dto.AccessRequestResponse;
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
import com.paypilot.entity.AppUser;
import com.paypilot.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 15;

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            EmailService emailService) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        logger.info(
                "Registration request received for username: {}",
                request.getUsername()
        );

        if (appUserRepository.existsByUsername(request.getUsername())) {

            logger.warn(
                    "Registration rejected because username already exists: {}",
                    request.getUsername()
            );

            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {

            logger.warn(
                    "Registration rejected because email already exists"
            );

            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        AppUser user = new AppUser();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setStatus("PENDING");
        user.setRole("USER");
        user.setFailedLoginAttempts(0);

        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        AppUser savedUser =
                appUserRepository.save(user);

        logger.info(
                "Registration request created successfully for username: {}",
                savedUser.getUsername()
        );

        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getStatus(),
                "Registration request submitted for admin approval"
        );
    }

    @Transactional
    public AccessRequestResponse decideAccessRequest(
            Long userId,
            AccessRequestDecisionRequest request) {

        logger.info(
                "Processing access request decision for userId: {}",
                userId
        );

        AppUser user =
                appUserRepository.findById(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found"
                                ));

        if (!"PENDING".equals(user.getStatus())) {

            throw new IllegalArgumentException(
                    "Only PENDING access requests can be approved or rejected"
            );
        }

        String decision = request.getStatus();

        user.setStatus(decision);
        user.setUpdatedAt(LocalDateTime.now());

        AppUser updatedUser =
                appUserRepository.save(user);

        logger.info(
                "Access request decision completed for userId: {} with status: {}",
                userId,
                decision
        );

        return new AccessRequestResponse(
                updatedUser.getUserId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getFullName(),
                updatedUser.getStatus(),
                updatedUser.getRole()
        );
    }

    
    public LoginResponse login(LoginRequest request) {

        logger.info(
                "Login attempt received for username: {}",
                request.getUsername()
        );

        AppUser user =
                appUserRepository
                        .findByUsername(request.getUsername())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid username or password"
                                ));

        LocalDateTime now =
                LocalDateTime.now();

        if (user.getLockedUntil() != null
                && now.isBefore(user.getLockedUntil())) {

            logger.warn(
                    "Login rejected because account is locked for username: {}",
                    user.getUsername()
            );

            throw new IllegalArgumentException(
                    "Account is locked. Please try again later."
            );
        }

        if (user.getLockedUntil() != null
                && !now.isBefore(user.getLockedUntil())) {

            user.setLockedUntil(null);
            user.setFailedLoginAttempts(0);
            user.setUpdatedAt(now);

            appUserRepository.save(user);
        }

        if (!"APPROVED".equals(user.getStatus())) {

            logger.warn(
                    "Login rejected because user is not approved: {}",
                    user.getUsername()
            );

            throw new IllegalArgumentException(
                    "User account is not approved"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            int failedAttempts =
                    user.getFailedLoginAttempts() == null
                            ? 0
                            : user.getFailedLoginAttempts();

            failedAttempts++;

            user.setFailedLoginAttempts(failedAttempts);
            user.setUpdatedAt(now);

            if (failedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {

                user.setLockedUntil(
                        now.plusMinutes(
                                LOCK_DURATION_MINUTES
                        )
                );

                appUserRepository.save(user);

                logger.warn(
                        "Account locked after {} failed login attempts for username: {}",
                        failedAttempts,
                        user.getUsername()
                );

                throw new IllegalArgumentException(
                        "Account locked after 3 failed login attempts. Please try again later."
                );
            }

            appUserRepository.save(user);

            logger.warn(
                    "Invalid password for username: {}. Failed attempts: {}",
                    user.getUsername(),
                    failedAttempts
            );

            throw new IllegalArgumentException(
                    "Invalid username or password"
            );
        }

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setUpdatedAt(now);

        appUserRepository.save(user);

        /*
         * Credentials are correct.
         * Generate the second authentication factor.
         */
        String otp =
                otpService.generateOtp(
                        user,
                        "LOGIN"
                );

        emailService.sendOtpEmail(
                user.getEmail(),
                "LOGIN",
                otp
        );

        logger.info(
                "Login credentials verified and LOGIN OTP requested for username: {}",
                user.getUsername()
        );

        return new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                "Credentials verified. OTP has been sent to your registered email."
        );
    }

    @Transactional
    public VerifyOtpResponse verifyOtp(
            VerifyOtpRequest request) {

        AppUser user =
                appUserRepository
                        .findByUsername(request.getUsername())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid username"
                                ));

        otpService.verifyOtp(
                user,
                request.getOtp(),
                request.getPurpose()
        );

        return new VerifyOtpResponse(
                user.getUsername(),
                request.getPurpose(),
                "OTP verified successfully",
                true
        );
    }

    @Transactional
    public ForgotPasswordResponse forgotPassword(
            ForgotPasswordRequest request) {

        AppUser user =
                appUserRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No account found for the provided email"
                                ));

        if (!"APPROVED".equals(user.getStatus())) {

            throw new IllegalArgumentException(
                    "User account is not approved"
            );
        }

        String otp =
                otpService.generateOtp(
                        user,
                        "PASSWORD_RESET"
                );

        emailService.sendOtpEmail(
                user.getEmail(),
                "PASSWORD_RESET",
                otp
        );

        logger.info(
                "Password reset OTP requested for userId: {}",
                user.getUserId()
        );

        return new ForgotPasswordResponse(
                "Password reset OTP has been sent to your registered email."
        );
    }

    @Transactional
    public ResetPasswordResponse resetPassword(
            ResetPasswordRequest request) {

        AppUser user =
                appUserRepository
                        .findByUsername(request.getUsername())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid username"
                                ));

        otpService.verifyOtp(
                user,
                request.getOtp(),
                "PASSWORD_RESET"
        );

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setUpdatedAt(LocalDateTime.now());

        appUserRepository.save(user);

        logger.info(
                "Password reset completed successfully for userId: {}",
                user.getUserId()
        );

        return new ResetPasswordResponse(
                user.getUsername(),
                "Password reset successfully"
        );
    }
}