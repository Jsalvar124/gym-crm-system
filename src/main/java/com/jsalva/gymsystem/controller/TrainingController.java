package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainingRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainingRequestDto;
import com.jsalva.gymsystem.dto.response.ErrorResponseDto;
import com.jsalva.gymsystem.dto.response.TrainingResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private final GymFacade gymFacade;

    private final AuthService authService;


    public TrainingController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }

    @Operation(
            summary = "Create a new training session",
            description = "Creates a new training session. Requires a valid trainer token and training details in the request body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK - Training session created successfully", content = @Content), // No body
            @ApiResponse(responseCode = "400", description = "Bad Request - invalid body or missing field",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only POST is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - business logic validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<Void> createTraining(@Valid @RequestBody CreateTrainingRequestDto requestDto){
        authService.validateTrainerAuth();
        gymFacade.createTraining(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update training session",
            description = "Updates an existing training session by ID. Requires a valid trainer token and updated training details in the request body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK - Training session updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - invalid body or missing field",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - training session with specified ID not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only PUT is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - business logic validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TrainingResponseDto> updateTraining(@Valid @RequestBody UpdateTrainingRequestDto requestDto, @PathVariable("id") Long id) {
        authService.validateTrainerAuth();
        TrainingResponseDto responseDto = gymFacade.updateTraining(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
