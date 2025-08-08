package com.think.health_first_server.controller;

import com.think.health_first_server.dao.Provider;
import com.think.health_first_server.dto.LoginRequest;
import com.think.health_first_server.dto.RegisterRequest;
import com.think.health_first_server.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        Provider provider = providerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Provider registered successfully. Verification email sent.",
                "data", Map.of(
                        "provider_id", provider.getId(),
                        "email", provider.getEmail(),
                        "verification_status", provider.getVerificationStatus()
                )
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String jwt = providerService.login(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "data", Map.of(
                        "access_token", jwt,
                        "expires_in", 3600,
                        "token_type", "Bearer"
                )
        ));
    }
}

