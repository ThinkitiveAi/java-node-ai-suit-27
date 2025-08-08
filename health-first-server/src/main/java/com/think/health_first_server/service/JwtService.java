package com.think.health_first_server.service;

import com.think.health_first_server.dao.Patient;
import com.think.health_first_server.dao.Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final String secret = "supersecretkey";
    private final long jwtExpiration = 3600000; // 1 hour
    private final long patientTokenExpiry = 1800000;  // 30 mins

    public String generateToken(Provider provider) {
        return Jwts.builder()
                .setSubject(provider.getId().toString())
                .claim("email", provider.getEmail())
                .claim("role", "provider")
                .claim("specialization", provider.getSpecialization())
                .claim("verification_status", provider.getVerificationStatus().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Patient patient) {
        return Jwts.builder()
                .setSubject(patient.getId().toString())
                .claim("email", patient.getEmail())
                .claim("role", "patient")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + patientTokenExpiry))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
