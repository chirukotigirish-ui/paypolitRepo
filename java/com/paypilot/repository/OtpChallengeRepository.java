package com.paypilot.repository;

import com.paypilot.entity.AppUser;
import com.paypilot.entity.OtpChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpChallengeRepository
        extends JpaRepository<OtpChallenge, Long> {

    Optional<OtpChallenge> findTopByUserAndPurposeAndUsedFalseOrderByCreatedAtDesc(
            AppUser user,
            String purpose
    );
}