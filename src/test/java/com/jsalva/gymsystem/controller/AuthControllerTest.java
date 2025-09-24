package com.jsalva.gymsystem.controller;


import com.jsalva.gymsystem.controller.advise.GlobalExceptionHandler;
import com.jsalva.gymsystem.exception.InvalidCredentialsException;
import com.jsalva.gymsystem.facade.GymFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.Mockito.when;

public class AuthControllerTest {
    private MockMvc mockMvc;
    private GymFacade gymFacade;

    @BeforeEach
    void setup() {
        gymFacade = Mockito.mock(GymFacade.class);
        AuthController authController = new AuthController(gymFacade);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void login_ShouldReturnSessionId_WhenCredentialsValid() throws Exception {
        when(gymFacade.login("Juan.Perez", "DyuQZ7wU1n"))
                .thenReturn("test-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username":"Juan.Perez",
                              "password":"DyuQZ7wU1n"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenInvalidCredentials() throws Exception {
        // Arrange: mock facade to simulate invalid login
        when(gymFacade.login("wrongUser", "wrongPass"))
                .thenThrow(new InvalidCredentialsException("Invalid credentials for username wrongUser"));

        // Act + Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "username": "wrongUser",
                  "password": "wrongPass"
                }
                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Invalid credentials for username wrongUser"));
    }

}
