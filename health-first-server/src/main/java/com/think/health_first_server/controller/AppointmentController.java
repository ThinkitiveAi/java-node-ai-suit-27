package com.think.health_first_server.controller;

import com.think.health_first_server.dao.Appointment;
import com.think.health_first_server.dto.AppointmentRequest;
import com.think.health_first_server.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.think.health_first_server.dto.AppointmentResponse;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        try {
            Appointment appointment = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Appointment booked successfully",
                    "data", Map.of(
                            "appointment_id", appointment.getId(),
                            "patient_name", appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                            "provider_name", appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName(),
                            "appointment_date_time", appointment.getAppointmentDateTime(),
                            "appointment_mode", appointment.getAppointmentMode(),
                            "appointment_type", appointment.getAppointmentType(),
                            "status", appointment.getStatus()
                    )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to book appointment: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            List<AppointmentResponse> appointmentResponses = appointments.stream()
                    .map(AppointmentResponse::fromAppointment)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Appointments retrieved successfully",
                    "data", appointmentResponses
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to retrieve appointments: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable UUID patientId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Patient appointments retrieved successfully",
                    "data", appointments
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to retrieve patient appointments: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<?> getAppointmentsByProvider(@PathVariable UUID providerId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByProvider(providerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Provider appointments retrieved successfully",
                    "data", appointments
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to retrieve provider appointments: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Appointment retrieved successfully",
                    "data", appointment
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to retrieve appointment: " + e.getMessage()
            ));
        }
    }
    
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable UUID appointmentId,
            @RequestParam Appointment.AppointmentStatus status) {
        try {
            Appointment appointment = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Appointment status updated successfully",
                    "data", appointment
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to update appointment status: " + e.getMessage()
            ));
        }
    }
} 