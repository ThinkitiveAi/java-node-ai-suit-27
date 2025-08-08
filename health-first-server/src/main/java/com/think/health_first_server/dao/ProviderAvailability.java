package com.think.health_first_server.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "provider_availability")
@Getter
@Setter
public class ProviderAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String timezone;
    private boolean isRecurring = false;
    private String recurrencePattern; // daily/weekly/monthly
    private LocalDate recurrenceEndDate;
    private int slotDuration = 30;
    private int breakDuration = 0;
    private String status = "available"; // available/booked/cancelled/blocked/maintenance
    private int maxAppointmentsPerSlot = 1;
    private int currentAppointments = 0;
    private String appointmentType = "consultation";
    private String locationType; // clinic/hospital/telemedicine/home_visit
    private String locationAddress;
    private String roomNumber;
    private Double baseFee;
    private Boolean insuranceAccepted;
    private String currency = "USD";
    private String notes;
    private String specialRequirements;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
