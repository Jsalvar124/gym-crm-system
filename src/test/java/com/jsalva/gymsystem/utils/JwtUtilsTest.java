package com.jsalva.gymsystem.utils;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private final JwtUtils jwtUtils = new JwtUtils();

    @Test
    void generateJwtToken_ShouldReturnValidToken() {
        String token = jwtUtils.generateJwtToken("testuser", "TRAINER");
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void validateJwtToken_ShouldReturnTrue_ForValidToken() {
        String token = jwtUtils.generateJwtToken("testuser", "TRAINER");
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_ForInvalidToken() {
        assertFalse(jwtUtils.validateJwtToken("invalid.token.here"));
        assertFalse(jwtUtils.validateJwtToken(null));
    }

    @Test
    void getUsernameFromJwtToken_ShouldReturnCorrectUsername() {
        String token = jwtUtils.generateJwtToken("testuser", "TRAINER");
        assertEquals("testuser", jwtUtils.getUsernameFromJwtToken(token));
    }

    @Test
    void getUserTypeFromJwtToken_ShouldReturnCorrectUserType() {
        String token = jwtUtils.generateJwtToken("testuser", "TRAINER");
        assertEquals("TRAINER", jwtUtils.getUserTypeFromJwtToken(token));
    }

    @Test
    void getUsernameFromJwtToken_ShouldThrowException_ForInvalidToken() {
        assertThrows(JwtException.class, () -> {
            jwtUtils.getUsernameFromJwtToken("invalid.token");
        });
    }

    @Test
    void getUserTypeFromJwtToken_ShouldThrowException_ForInvalidToken() {
        assertThrows(JwtException.class, () -> {
            jwtUtils.getUserTypeFromJwtToken("invalid.token");
        });
    }


}
