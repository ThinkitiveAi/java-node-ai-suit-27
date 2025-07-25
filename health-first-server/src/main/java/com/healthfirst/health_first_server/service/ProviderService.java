package com.healthfirst.health_first_server.service;

import com.healthfirst.health_first_server.entity.Provider;
import com.healthfirst.health_first_server.repository.ProviderRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Set<String> ALLOWED_SPECIALIZATIONS = Set.of(
            "Cardiology", "Dermatology", "Pediatrics", "Orthopedics", "Neurology", "General Medicine"
    );
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public Provider registerProvider(Provider provider, String rawPassword, String confirmPassword) {
        // Specialization check
        if (!ALLOWED_SPECIALIZATIONS.contains(provider.getSpecialization())) {
            throw new IllegalArgumentException("Invalid specialization");
        }
        // Password match and strength
        if (!rawPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (!isPasswordStrong(rawPassword)) {
            throw new IllegalArgumentException("Password does not meet strength requirements");
        }
        // Duplicate checks
        if (providerRepository.existsByEmail(provider.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (providerRepository.existsByPhoneNumber(provider.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        if (providerRepository.existsByLicenseNumber(provider.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists");
        }
        // Hash password and set required fields
        provider.setPasswordHash(passwordEncoder.encode(rawPassword));
        provider.setVerificationStatus(Provider.VerificationStatus.PENDING);
        provider.setActive(true);
        // Now validate the entity
        Set<ConstraintViolation<Provider>> violations = validator.validate(provider);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        // Save
        return providerRepository.save(provider);
    }

    private boolean isPasswordStrong(String password) {
        // 8+ chars, upper, lower, number, special
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[^a-zA-Z0-9].*");
    }
} 