package com.think.health_first_server.dto;

import com.think.health_first_server.dao.ClinicAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    @NotBlank
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

    @Min(0)
    @Max(50)
    private int yearsOfExperience;

    @Valid
    private ClinicAddress clinicAddress;
}

