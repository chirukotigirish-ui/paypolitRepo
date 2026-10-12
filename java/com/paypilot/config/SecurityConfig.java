package com.paypilot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Registration
                .requestMatchers("/api/users/register").permitAll()

                // Login credentials
                .requestMatchers("/api/auth/login").permitAll()

                // OTP verification
                .requestMatchers("/api/auth/verify-otp").permitAll()

                // Forgot password
                .requestMatchers("/api/auth/forgot-password").permitAll()

                // Reset password
                .requestMatchers("/api/auth/reset-password").permitAll()

                // Admin access-request APIs
                .requestMatchers("/api/admin/**").permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }
}