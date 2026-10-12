package com.paypilot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String senderEmail) {

        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendOtpEmail(
            String email,
            String purpose,
            String otp) {

        if (!"LOGIN".equals(purpose)
                && !"PASSWORD_RESET".equals(purpose)) {
            throw new IllegalArgumentException(
                    "Invalid OTP purpose");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(email);

        if ("LOGIN".equals(purpose)) {
            message.setSubject(
                    "PayPilot Login Verification OTP");
            message.setText(
                    "Hello,\n\n"
                    + "Your PayPilot login verification OTP is: "
                    + otp
                    + "\n\nThis OTP expires in 5 minutes."
                    + "\n\nIf you did not request this code, "
                    + "please ignore this email.");
        } else {
            message.setSubject(
                    "PayPilot Password Reset OTP");
            message.setText(
                    "Hello,\n\n"
                    + "Your PayPilot password reset OTP is: "
                    + otp
                    + "\n\nThis OTP expires in 5 minutes."
                    + "\n\nIf you did not request this code, "
                    + "please ignore this email.");
        }

        mailSender.send(message);
    }
}