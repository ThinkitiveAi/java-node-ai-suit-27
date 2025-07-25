package com.healthfirst.health_first_server.security;

import com.healthfirst.health_first_server.entity.Provider;
import com.healthfirst.health_first_server.repository.ProviderRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ProviderRepository providerRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ProviderRepository providerRepository) {
        this.jwtUtil = jwtUtil;
        this.providerRepository = providerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt)) {
            try {
                Claims claims = jwtUtil.getAllClaims(jwt);
                String email = claims.get("email", String.class);
                Optional<Provider> providerOpt = providerRepository.findByEmail(email);
                if (providerOpt.isPresent() && providerOpt.get().isActive() && providerOpt.get().getVerificationStatus() == Provider.VerificationStatus.VERIFIED) {
                    Provider provider = providerOpt.get();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            provider, null, Collections.emptyList()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                // Invalid or expired token, do nothing (request will be unauthenticated)
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 