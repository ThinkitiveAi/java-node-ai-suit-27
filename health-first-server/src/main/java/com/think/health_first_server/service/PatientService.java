package com.think.health_first_server.service;

import com.think.health_first_server.dao.Patient;
import com.think.health_first_server.dto.LoginRequest;
import com.think.health_first_server.dto.PatientRegisterRequest;
import com.think.health_first_server.repo.PatientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PatientService {
    @Autowired
    private PatientRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Patient register(PatientRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (Period.between(request.getDateOfBirth(), LocalDate.now()).getYears() < 13) {
            throw new IllegalArgumentException("Must be at least 13 years old");
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Patient patient = new Patient();
        BeanUtils.copyProperties(request, patient);
        patient.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        return repository.save(patient);
    }

    public String login(LoginRequest request) {
        Patient patient = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), patient.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!patient.isActive()) {
            throw new AccessDeniedException("Account is inactive");
        }

        return jwtService.generateToken(patient);
    }
}

