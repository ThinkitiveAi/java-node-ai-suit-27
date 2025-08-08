package com.think.health_first_server.service;

import com.think.health_first_server.dao.Provider;
import com.think.health_first_server.dto.LoginRequest;
import com.think.health_first_server.dto.RegisterRequest;
import com.think.health_first_server.enums.VerificationStatus;
import com.think.health_first_server.repo.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    @Autowired private ProviderRepository repository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Provider register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match");

        if (repository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        // repeat for phoneNumber and licenseNumber...

        Provider provider = new Provider();
        provider.setFirstName(request.getFirstName());
        provider.setLastName(request.getLastName());
        provider.setEmail(request.getEmail());
        provider.setPhoneNumber(request.getPhoneNumber());
        provider.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        provider.setSpecialization(request.getSpecialization());
        provider.setLicenseNumber(request.getLicenseNumber());
        provider.setYearsOfExperience(request.getYearsOfExperience());
        provider.setClinicAddress(request.getClinicAddress());
        return repository.save(provider);
    }

    public String login(LoginRequest loginRequest) {
        Provider provider = repository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), provider.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!provider.isActive() || provider.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new AccessDeniedException("Account not active or verified");
        }

        return jwtService.generateToken(provider);
    }
}

