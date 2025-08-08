package com.think.health_first_server.service;

import com.think.health_first_server.dao.Provider;
import com.think.health_first_server.dao.ProviderAvailability;
import com.think.health_first_server.dto.ProviderAvailabilityRequest;
import com.think.health_first_server.repo.ProviderAvailabilityRepository;
import com.think.health_first_server.repo.ProviderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderAvailabilityService {
    @Autowired
    private ProviderAvailabilityRepository availabilityRepository;
    @Autowired
    private ProviderRepository providerRepository;

    public ProviderAvailability createAvailability(ProviderAvailabilityRequest request) {
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
        ProviderAvailability availability = new ProviderAvailability();
        BeanUtils.copyProperties(request, availability);
        availability.setProvider(provider);
        return availabilityRepository.save(availability);
    }

    public ProviderAvailability getAvailability(java.util.UUID id) {
        return availabilityRepository.findById(id).orElse(null);
    }

    public ProviderAvailability updateAvailability(java.util.UUID id, ProviderAvailabilityRequest request) {
        ProviderAvailability availability = availabilityRepository.findById(id).orElse(null);
        if (availability == null) return null;
        BeanUtils.copyProperties(request, availability);
        // Ensure provider is set correctly
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
        availability.setProvider(provider);
        return availabilityRepository.save(availability);
    }

    public boolean deleteAvailability(java.util.UUID id) {
        if (!availabilityRepository.existsById(id)) return false;
        availabilityRepository.deleteById(id);
        return true;
    }
}
