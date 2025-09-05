//package com.jsalva.gymsystem.utils;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.beans.Encoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UtilsTest {
//    @Test
//    @DisplayName("Should generate password with correct format")
//    void shouldGeneratePasswordWithCorrectFormat() {
//        // When - call the method multiple times
//        String password1 = UserUtils.generateRandomPassword();
//        String password2 = UserUtils.generateRandomPassword();
//
//        // Then - verify format
//        assertEquals(10, password1.length());
//        assertEquals(10, password2.length());
//        assertTrue(password1.matches("[a-zA-Z0-9]{10}"));
//        assertTrue(password2.matches("[a-zA-Z0-9]{10}"));
//        assertNotEquals(password1, password2); // Should be different
//    }
//
//    @Test
//    @DisplayName("Test valid credentials")
//    void shouldReturnTrueForValidCredentials(){
//        String plainPassword = "newPassword";
//        String hashedPassword = "$2a$10$fA54/oCBsoUF15NuL1WOs.hA5CjP1v/M8USdioI7saEKe5njhjqAS";
//
//        assertTrue(EncoderUtils.verifyPassword(plainPassword,hashedPassword));
//        assertFalse(EncoderUtils.verifyPassword(plainPassword,"someUnhashedText"));
//    }
//
//    @Test
//    @DisplayName("Test valid different valid hash generation")
//    void shouldGenerateDifferentValidHashes() {
//        String password = "password";
//
//        String hash1 = EncoderUtils.encryptPassword(password);
//        String hash2 = EncoderUtils.encryptPassword(password);
//
//        assertNotEquals(hash1,hash2);
//        assertTrue(EncoderUtils.verifyPassword(password,hash1));
//        assertTrue(EncoderUtils.verifyPassword(password,hash2));
//
//    }
//}
