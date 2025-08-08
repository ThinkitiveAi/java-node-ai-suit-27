package com.think.health_first_server.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;
    
    @Column(name = "appointment_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentMode appointmentMode;
    
    @Column(name = "appointment_type", nullable = false)
    private String appointmentType;
    
    @Column(name = "estimated_amount")
    private Double estimatedAmount;
    
    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;
    
    @Column(name = "reason_for_visit", columnDefinition = "TEXT")
    private String reasonForVisit;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum AppointmentMode {
        IN_PERSON, VIDEO_CALL, HOME
    }
    
    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
    }
} 