package com.paypilot.controller;

import com.paypilot.dto.AccessRequestDecisionRequest;
import com.paypilot.dto.AccessRequestResponse;
import com.paypilot.entity.AppUser;
import com.paypilot.repository.AppUserRepository;
import com.paypilot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AppUserRepository appUserRepository;
    private final AuthService authService;

    public AdminController(
            AppUserRepository appUserRepository,
            AuthService authService) {

        this.appUserRepository = appUserRepository;
        this.authService = authService;
    }

    @GetMapping("/access-requests")
    public List<AccessRequestResponse> getAccessRequests() {

        return appUserRepository.findAll()
                .stream()
                .filter(user -> "PENDING".equals(user.getStatus()))
                .map(this::toAccessRequestResponse)
                .toList();
    }

    @PatchMapping("/access-requests/{userId}")
    public AccessRequestResponse decideAccessRequest(
            @PathVariable Long userId,
            @Valid @RequestBody AccessRequestDecisionRequest request) {

        return authService.decideAccessRequest(userId, request);
    }

    private AccessRequestResponse toAccessRequestResponse(
            AppUser user) {

        return new AccessRequestResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getStatus(),
                user.getRole()
        );
    }
}