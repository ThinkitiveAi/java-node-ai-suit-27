package com.healthfirst.health_first_server.controller;

import com.healthfirst.health_first_server.entity.Provider;
import com.healthfirst.health_first_server.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.healthfirst.health_first_server.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.healthfirst.health_first_server.repository.ProviderRepository;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/provider")
@RequiredArgsConstructor
public class ProviderController {
    private final ProviderService providerService;
    private final ProviderRepository providerRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerProvider(@Valid @RequestBody ProviderRegistrationRequest request) {
        try {
            Provider provider = Provider.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .specialization(request.getSpecialization())
                    .licenseNumber(request.getLicenseNumber())
                    .yearsOfExperience(request.getYearsOfExperience())
                    .clinicStreet(request.getClinicAddress().getStreet())
                    .clinicCity(request.getClinicAddress().getCity())
                    .clinicState(request.getClinicAddress().getState())
                    .clinicZip(request.getClinicAddress().getZip())
                    .build();
            Provider saved = providerService.registerProvider(provider, request.getPassword(), request.getConfirmPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("provider_id", saved.getId());
            data.put("email", saved.getEmail());
            data.put("verification_status", saved.getVerificationStatus().name().toLowerCase());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Provider registered successfully. Verification email sent.",
                    "data", data
            ));
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null || request.getEmail().isBlank() || request.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid credentials",
                "error_code", "INVALID_CREDENTIALS"
            ));
        }
        Optional<Provider> providerOpt = providerRepository.findByEmail(request.getEmail());
        if (providerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid credentials",
                "error_code", "INVALID_CREDENTIALS"
            ));
        }
        Provider provider = providerOpt.get();
        if (!provider.isActive() || provider.getVerificationStatus() != Provider.VerificationStatus.VERIFIED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Account not active or not verified",
                "error_code", "ACCOUNT_NOT_ACTIVE_OR_VERIFIED"
            ));
        }
        if (!passwordEncoder.matches(request.getPassword(), provider.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid credentials",
                "error_code", "INVALID_CREDENTIALS"
            ));
        }
        Map<String, Object> claims = Map.of(
            "provider_id", provider.getId().toString(),
            "email", provider.getEmail(),
            "role", "provider",
            "specialization", provider.getSpecialization(),
            "verification_status", provider.getVerificationStatus().name().toLowerCase()
        );
        String token = jwtUtil.generateToken(claims, provider.getEmail());
        Map<String, Object> data = new HashMap<>();
        data.put("access_token", token);
        data.put("expires_in", 3600);
        data.put("token_type", "Bearer");
        data.put("provider", provider); // You may want to map to a DTO
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Login successful",
            "data", data
        ));
    }

    // DTOs
    @Getter
    @Setter
    public static class ProviderRegistrationRequest {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String phoneNumber;
        @NotBlank
        private String password;
        @NotBlank
        private String confirmPassword;
        @NotBlank
        private String specialization;
        @NotBlank
        private String licenseNumber;
        private Integer yearsOfExperience;
        @Valid
        private ClinicAddress clinicAddress;
        @Getter
        @Setter
        public static class ClinicAddress {
            @NotBlank
            private String street;
            @NotBlank
            private String city;
            @NotBlank
            private String state;
            @NotBlank
            private String zip;
        }
    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }
} 