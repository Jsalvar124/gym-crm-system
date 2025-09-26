package com.jsalva.gymsystem.security;

import com.jsalva.gymsystem.filter.JwtAuthenticationFilter;
import com.jsalva.gymsystem.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        SecurityContextHolder.clearContext(); // Clear security context before each test
    }

    @Test
    void doFilter_ShouldSetAuthentication_WithValidToken() throws ServletException, IOException {
        // Arrange
        String validToken = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtils.validateJwtToken(validToken)).thenReturn(true);
        when(jwtUtils.getUsernameFromJwtToken(validToken)).thenReturn("testuser");
        when(jwtUtils.getUserTypeFromJwtToken(validToken)).thenReturn("TRAINER");

        // Act - call doFilter instead of doFilterInternal
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("testuser", auth.getPrincipal());
    }

    @Test
    void doFilter_ShouldNotSetAuthentication_WithInvalidToken() throws ServletException, IOException {
        // Arrange
        String invalidToken = "invalid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtils.validateJwtToken(invalidToken)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

}
