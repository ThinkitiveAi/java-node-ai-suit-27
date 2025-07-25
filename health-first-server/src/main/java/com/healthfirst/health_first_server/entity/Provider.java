package com.healthfirst.health_first_server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "provider", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number"),
        @UniqueConstraint(columnNames = "license_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank
    private String passwordHash;

    @Column(name = "specialization", nullable = false, length = 100)
    @NotBlank
    @Size(min = 3, max = 100)
    private String specialization;

    @Column(name = "license_number", nullable = false, length = 50, unique = true)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "License number must be alphanumeric")
    private String licenseNumber;

    @Column(name = "years_of_experience")
    @Min(0)
    @Max(50)
    private Integer yearsOfExperience;

    @Column(name = "clinic_street", nullable = false, length = 200)
    @NotBlank
    @Size(max = 200)
    private String clinicStreet;

    @Column(name = "clinic_city", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String clinicCity;

    @Column(name = "clinic_state", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String clinicState;

    @Column(name = "clinic_zip", nullable = false, length = 20)
    @NotBlank
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid postal code")
    private String clinicZip;

    @Column(name = "verification_status", nullable = false, length = 20)
    @NotNull
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum VerificationStatus {
        PENDING, VERIFIED, REJECTED
    }
} 