package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.ChangePasswordRequestDto;
import com.jsalva.gymsystem.dto.request.LoginRequestDto;
import com.jsalva.gymsystem.dto.response.ErrorResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    GymFacade gymFacade;

    public AuthController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Operation(
            summary = "Authenticate a user",
            description = "Logs in a user with username and password and returns a Json Web token (JWT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKdWFuLlBlcmV6IiwidXNlclR5cGUiOiJUUkFJTkVSIiwiaWF0IjoxNzU4NTEzOTUyLCJleHAiOjE3NTg2MDAzNTJ9.MtxEmM01-tEo5704nVrEiNaxiHB3e3jkgvndbkN73Gg\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only POST is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        String token = gymFacade.login(loginRequestDto.username(), loginRequestDto.password());
        return ResponseEntity.ok(Map.of("token", token));
    }


    @Operation(summary = "Change user password",
            description = "Updates the current user's password. The new password must meet validation rules (e.g., contain at least one letter and one number).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content), // no body returned
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only PUT is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/users/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto){
        gymFacade.updateUserPassword(requestDto);
        return ResponseEntity.ok().build();
    }

}
