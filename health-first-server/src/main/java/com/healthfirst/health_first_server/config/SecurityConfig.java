package com.healthfirst.health_first_server.config;

import com.healthfirst.health_first_server.security.JwtAuthenticationFilter;
import com.healthfirst.health_first_server.repository.ProviderRepository;
import com.healthfirst.health_first_server.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final ProviderRepository providerRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfig(ProviderRepository providerRepository, JwtUtil jwtUtil) {
        this.providerRepository = providerRepository;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/provider/register", "/api/v1/provider/login", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // for H2 console
            .formLogin(form -> form.disable())
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, providerRepository), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
} 