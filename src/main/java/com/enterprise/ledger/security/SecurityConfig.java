package com.enterprise.ledger.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the ledger microservice.
 * Enforces a stateless non-blocking security pipeline across virtual threads.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable Cross-Site Request Forgery (CSRF) for our stateless REST API architecture
            .csrf(csrf -> http.csrf(c -> c.disable()))
            
            // 2. Enforce a stateless session policy (no HTTP sessions are allowed to reside in RAM)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Define path routing authentication parameters
            .authorizeHttpRequests(auth -> auth
                // Allow actuator health metrics to remain open for infrastructure monitoring
                .requestMatchers("/actuator/**").permitAll()
                // Explicitly lock down all financial ledger transaction endpoint routes
                .anyRequest().authenticated()
            )
            
            // 4. Inject our custom JWT token verification filter before the standard authentication processor
            .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}