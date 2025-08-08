package com.think.health_first_server.controller;

import com.think.health_first_server.dao.Patient;
import com.think.health_first_server.dto.LoginRequest;
import com.think.health_first_server.dto.PatientRegisterRequest;
import com.think.health_first_server.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient")
@Validated
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PatientRegisterRequest request) {
        Patient patient = patientService.register(request);
        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "message", "Patient registered successfully. Verification email sent.",
                        "patient_id", patient.getId()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = patientService.login(request);
        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "success", true,
                        "message", "Login successful",
                        "data", java.util.Map.of(
                                "access_token", token,
                                "token_type", "Bearer"
                        )
                )
        );
    }
}
