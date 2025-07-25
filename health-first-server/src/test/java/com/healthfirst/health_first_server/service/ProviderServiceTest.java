package com.healthfirst.health_first_server.service;

import com.healthfirst.health_first_server.entity.Provider;
import com.healthfirst.health_first_server.repository.ProviderRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProviderServiceTest {
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private ProviderService providerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        providerService = new ProviderService(providerRepository, passwordEncoder);
    }

    private Provider.ProviderBuilder sampleProviderBuilder() {
        return Provider.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@clinic.com")
                .phoneNumber("+1234567890")
                .specialization("Cardiology")
                .licenseNumber("MD123456789")
                .yearsOfExperience(10)
                .clinicStreet("123 Medical Center Dr")
                .clinicCity("New York")
                .clinicState("NY")
                .clinicZip("10001");
    }

    @Test
    void testRegisterProvider_Success() {
        Provider provider = sampleProviderBuilder().build();
        when(providerRepository.existsByEmail(anyString())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(providerRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(providerRepository.save(any(Provider.class))).thenAnswer(i -> i.getArgument(0));

        Provider saved = providerService.registerProvider(provider, "Password1!", "Password1!");
        assertEquals("hashed", saved.getPasswordHash());
        assertEquals(Provider.VerificationStatus.PENDING, saved.getVerificationStatus());
        assertTrue(saved.isActive());
    }

    @Test
    void testRegisterProvider_DuplicateEmail() {
        Provider provider = sampleProviderBuilder().build();
        when(providerRepository.existsByEmail(anyString())).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "Password1!", "Password1!")
        );
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void testRegisterProvider_DuplicatePhone() {
        Provider provider = sampleProviderBuilder().build();
        when(providerRepository.existsByEmail(anyString())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(anyString())).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "Password1!", "Password1!")
        );
        assertEquals("Phone number already exists", ex.getMessage());
    }

    @Test
    void testRegisterProvider_DuplicateLicense() {
        Provider provider = sampleProviderBuilder().build();
        when(providerRepository.existsByEmail(anyString())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(providerRepository.existsByLicenseNumber(anyString())).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "Password1!", "Password1!")
        );
        assertEquals("License number already exists", ex.getMessage());
    }

    @Test
    void testRegisterProvider_InvalidPassword() {
        Provider provider = sampleProviderBuilder().build();
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "weak", "weak")
        );
        assertEquals("Password does not meet strength requirements", ex.getMessage());
    }

    @Test
    void testRegisterProvider_PasswordMismatch() {
        Provider provider = sampleProviderBuilder().build();
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "Password1!", "Password2!")
        );
        assertEquals("Passwords do not match", ex.getMessage());
    }

    @Test
    void testRegisterProvider_InvalidSpecialization() {
        Provider provider = sampleProviderBuilder().specialization("Unknown").build();
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                providerService.registerProvider(provider, "Password1!", "Password1!")
        );
        assertEquals("Invalid specialization", ex.getMessage());
    }

    @Test
    void testPasswordHashingAndVerification() {
        Provider provider = sampleProviderBuilder().build();
        when(providerRepository.existsByEmail(anyString())).thenReturn(false);
        when(providerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(providerRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(providerRepository.save(any(Provider.class))).thenAnswer(i -> i.getArgument(0));

        Provider saved = providerService.registerProvider(provider, "Password1!", "Password1!");
        assertEquals("hashed", saved.getPasswordHash());
        // Simulate password verification
        when(passwordEncoder.matches("Password1!", "hashed")).thenReturn(true);
        assertTrue(passwordEncoder.matches("Password1!", saved.getPasswordHash()));
    }
} 