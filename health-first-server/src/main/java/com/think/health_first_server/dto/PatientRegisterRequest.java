package com.think.health_first_server.dto;

import com.think.health_first_server.dao.Address;
import com.think.health_first_server.dao.EmergencyContact;
import com.think.health_first_server.dao.InsuranceInfo;
import com.think.health_first_server.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PatientRegisterRequest {
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

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @Valid
    private Address address;

    @Valid
    private EmergencyContact emergencyContact;

    private List<String> medicalHistory;

    @Valid
    private InsuranceInfo insuranceInfo;
}

