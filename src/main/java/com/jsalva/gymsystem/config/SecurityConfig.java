package com.jsalva.gymsystem.config;

import com.jsalva.gymsystem.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // handles Unauthorized errors before reaching the controller advise.
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            String jsonResponse = """
            {
                "error": "Unauthorized",
                "message": "Authentication required - please provide a valid token",
                "timestamp": "%s",
                "status": 401
            }
            """.formatted(LocalDateTime.now().toString());

            response.getWriter().write(jsonResponse);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
                .cors(Customizer.withDefaults())   // <-- enable CORS support
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
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint()) // Handle error before controller advise
                )
                // Add custom JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );

        return http.build();
    }

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${app.cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${app.cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${app.cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${app.cors.max-age}")
    private long maxAge;

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(allowedMethods);
        config.setAllowedHeaders(allowedHeaders);
        config.setAllowCredentials(allowCredentials);
        config.setMaxAge(maxAge);

        // if you also need to expose headers like Authorization
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
