package com.think.health_first_server.controller;

import com.think.health_first_server.dao.ProviderAvailability;
import com.think.health_first_server.dto.ProviderAvailabilityRequest;
import com.think.health_first_server.service.ProviderAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/provider/availability")
public class ProviderAvailabilityController {
    @Autowired
    private ProviderAvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<?> createAvailability(@Valid @RequestBody ProviderAvailabilityRequest request) {
        ProviderAvailability availability = availabilityService.createAvailability(request);
        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "message", "Availability slot created successfully.",
                        "availability_id", availability.getId()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAvailability(@PathVariable("id") java.util.UUID id) {
        ProviderAvailability availability = availabilityService.getAvailability(id);
        if (availability == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(availability);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAvailability(@PathVariable("id") java.util.UUID id,
                                                @Valid @RequestBody ProviderAvailabilityRequest request) {
        ProviderAvailability updated = availabilityService.updateAvailability(id, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAvailability(@PathVariable("id") java.util.UUID id) {
        boolean deleted = availabilityService.deleteAvailability(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(java.util.Map.of("message", "Availability deleted"));
    }
}
