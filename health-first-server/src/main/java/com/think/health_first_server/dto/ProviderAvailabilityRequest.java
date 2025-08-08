package com.think.health_first_server.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class ProviderAvailabilityRequest {
    private UUID providerId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String timezone;
    private boolean isRecurring;
    private String recurrencePattern;
    private LocalDate recurrenceEndDate;
    private int slotDuration;
    private int breakDuration;
    private String appointmentType;
    private String locationType;
    private String locationAddress;
    private String roomNumber;
    private Double baseFee;
    private Boolean insuranceAccepted;
    private String currency;
    private String notes;
    private String specialRequirements;
}
