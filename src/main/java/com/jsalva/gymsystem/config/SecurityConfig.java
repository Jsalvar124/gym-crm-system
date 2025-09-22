package com.jsalva.gymsystem.config;

import com.jsalva.gymsystem.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login").permitAll() // Public login endpoint
                        .requestMatchers(HttpMethod.PUT, "/api/v1/auth/users/password").permitAll() // Public password change
                        .requestMatchers("/swagger-ui/**", "/api/v1/v3/api-docs/**").permitAll() // Public Swagger
                        .requestMatchers("/h2-console/**").permitAll() // Public Swagger
                        .requestMatchers("/actuator/**").permitAll() // Public Actuator
                        .requestMatchers(HttpMethod.POST, "/api/v1/trainers", "/api/v1/trainees").permitAll() // Public registration
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable basic auth
                .formLogin(form -> form.disable()) // Disable form login
                // ADD OUR JWT FILTER BEFORE Spring Security's authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        return http.build();
    }
}
