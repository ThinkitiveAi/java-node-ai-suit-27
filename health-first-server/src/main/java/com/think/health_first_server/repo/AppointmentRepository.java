package com.think.health_first_server.repo;

import com.think.health_first_server.dao.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    List<Appointment> findByPatientId(UUID patientId);
    
    List<Appointment> findByProviderId(UUID providerId);
    
    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId AND a.appointmentDateTime BETWEEN :startTime AND :endTime")
    List<Appointment> findByProviderAndTimeRange(@Param("providerId") UUID providerId, 
                                                @Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.provider.id = :providerId AND a.appointmentDateTime = :appointmentDateTime AND a.status NOT IN ('CANCELLED')")
    long countConflictingAppointments(@Param("providerId") UUID providerId, 
                                     @Param("appointmentDateTime") LocalDateTime appointmentDateTime);
} 