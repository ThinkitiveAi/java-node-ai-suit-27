package com.think.health_first_server.dto;

import com.think.health_first_server.dao.Appointment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID providerId;
    private String providerName;
    private Appointment.AppointmentMode appointmentMode;
    private String appointmentType;
    private Double estimatedAmount;
    private LocalDateTime appointmentDateTime;
    private String reasonForVisit;
    private Appointment.AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static AppointmentResponse fromAppointment(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatient().getId());
        response.setPatientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName());
        response.setProviderId(appointment.getProvider().getId());
        response.setProviderName(appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName());
        response.setAppointmentMode(appointment.getAppointmentMode());
        response.setAppointmentType(appointment.getAppointmentType());
        response.setEstimatedAmount(appointment.getEstimatedAmount());
        response.setAppointmentDateTime(appointment.getAppointmentDateTime());
        response.setReasonForVisit(appointment.getReasonForVisit());
        response.setStatus(appointment.getStatus());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }
} 