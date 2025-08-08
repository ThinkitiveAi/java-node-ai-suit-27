package com.think.health_first_server.service;

import com.think.health_first_server.dao.Appointment;
import com.think.health_first_server.dao.Patient;
import com.think.health_first_server.dao.Provider;
import com.think.health_first_server.dto.AppointmentRequest;
import com.think.health_first_server.repo.AppointmentRepository;
import com.think.health_first_server.repo.PatientRepository;
import com.think.health_first_server.repo.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    public Appointment createAppointment(AppointmentRequest request) {
        // Validate patient exists
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        
        // Validate provider exists
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
        
        // Check if appointment time is in the future
        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time cannot be in the past");
        }
        
        // Check for conflicting appointments
        long conflictingAppointments = appointmentRepository.countConflictingAppointments(
                request.getProviderId(), request.getAppointmentDateTime());
        
        if (conflictingAppointments > 0) {
            throw new IllegalArgumentException("Provider has a conflicting appointment at this time");
        }
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setProvider(provider);
        appointment.setAppointmentMode(request.getAppointmentMode());
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setEstimatedAmount(request.getEstimatedAmount());
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        
        return appointmentRepository.save(appointment);
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getAppointmentsByPatient(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByProvider(UUID providerId) {
        return appointmentRepository.findByProviderId(providerId);
    }
    
    public Appointment getAppointmentById(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }
    
    public Appointment updateAppointmentStatus(UUID appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
} 