package com.paypilot.service;

import com.paypilot.entity.AppUser;
import com.paypilot.entity.OtpChallenge;
import com.paypilot.repository.OtpChallengeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static final Logger logger =
            LoggerFactory.getLogger(OtpService.class);

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;

    private final OtpChallengeRepository otpChallengeRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(
            OtpChallengeRepository otpChallengeRepository,
            PasswordEncoder passwordEncoder) {

        this.otpChallengeRepository = otpChallengeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String generateOtp(
            AppUser user,
            String purpose) {

        validatePurpose(purpose);

        LocalDateTime now = LocalDateTime.now();

        /*
         * Invalidate any previous unused OTP for the same
         * user and purpose by marking it as used.
         */
        otpChallengeRepository
                .findTopByUserAndPurposeAndUsedFalseOrderByCreatedAtDesc(
                        user,
                        purpose
                )
                .ifPresent(previousOtp -> {
                    previousOtp.setUsed(true);
                    otpChallengeRepository.save(previousOtp);
                });

        String otp = generateSixDigitOtp();

        OtpChallenge otpChallenge = new OtpChallenge();

        otpChallenge.setUser(user);
        otpChallenge.setOtpHash(
                passwordEncoder.encode(otp)
        );
        otpChallenge.setPurpose(purpose);
        otpChallenge.setExpiresAt(
                now.plusMinutes(OTP_EXPIRY_MINUTES)
        );
        otpChallenge.setUsed(false);
        otpChallenge.setCreatedAt(now);

        otpChallengeRepository.save(otpChallenge);

        logger.info(
                "OTP generated successfully for userId: {} with purpose: {}",
                user.getUserId(),
                purpose
        );

        /*
         * The actual OTP will later be sent through
         * the email service.
         *
         * We return it internally for now so the
         * authentication service can pass it to the
         * email-sending component.
         */
        return otp;
    }

    @Transactional
    public void verifyOtp(
            AppUser user,
            String otp,
            String purpose) {

        validatePurpose(purpose);

        OtpChallenge otpChallenge =
                otpChallengeRepository
                        .findTopByUserAndPurposeAndUsedFalseOrderByCreatedAtDesc(
                                user,
                                purpose
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid or expired OTP"
                                ));

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(otpChallenge.getExpiresAt())) {

            otpChallenge.setUsed(true);
            otpChallengeRepository.save(otpChallenge);

            logger.warn(
                    "Expired OTP verification attempt for userId: {} with purpose: {}",
                    user.getUserId(),
                    purpose
            );

            throw new IllegalArgumentException(
                    "Invalid or expired OTP"
            );
        }

        if (!passwordEncoder.matches(
                otp,
                otpChallenge.getOtpHash())) {

            logger.warn(
                    "Invalid OTP verification attempt for userId: {} with purpose: {}",
                    user.getUserId(),
                    purpose
            );

            throw new IllegalArgumentException(
                    "Invalid or expired OTP"
            );
        }

        otpChallenge.setUsed(true);
        otpChallengeRepository.save(otpChallenge);

        logger.info(
                "OTP verified successfully for userId: {} with purpose: {}",
                user.getUserId(),
                purpose
        );
    }

    private String generateSixDigitOtp() {

        int minimum = 100000;
        int maximum = 999999;

        int otpNumber =
                secureRandom.nextInt(
                        maximum - minimum + 1
                ) + minimum;

        return String.valueOf(otpNumber);
    }

    private void validatePurpose(String purpose) {

        if (!"LOGIN".equals(purpose)
                && !"PASSWORD_RESET".equals(purpose)) {

            throw new IllegalArgumentException(
                    "Invalid OTP purpose"
            );
        }
    }
}