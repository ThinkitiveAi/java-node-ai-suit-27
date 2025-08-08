package com.think.health_first_server.dto;

import com.think.health_first_server.dao.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRequest {
    
    @NotNull(message = "Patient ID is required")
    private UUID patientId;
    
    @NotNull(message = "Provider ID is required")
    private UUID providerId;
    
    @NotNull(message = "Appointment mode is required")
    private Appointment.AppointmentMode appointmentMode;
    
    @NotNull(message = "Appointment type is required")
    private String appointmentType;
    
    @Positive(message = "Estimated amount must be positive")
    private Double estimatedAmount;
    
    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDateTime;
    
    private String reasonForVisit;
} 