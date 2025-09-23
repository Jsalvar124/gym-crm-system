package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.ChangeStateRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTrainerRequestDto;
import com.jsalva.gymsystem.dto.request.TrainerTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTrainerRequestDto;
import com.jsalva.gymsystem.dto.response.*;
import com.jsalva.gymsystem.exception.BadRequestException;
import com.jsalva.gymsystem.facade.GymFacade;
import com.jsalva.gymsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/trainers")
public class TrainerController {
    private final GymFacade gymFacade;

    private final AuthService authService;

    public TrainerController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }


    @Operation(
            summary = "Create a new trainer",
            description = "Creates a new trainer with the provided details. "
                    + "The trainer's username and password are auto-generated. "
                    + "Returns the created trainer credentials and sets the `Location` header "
                    + "to the new resource's URI."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateTrainerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only POST is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - email already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<CreateTrainerResponseDto> createTrainer(@Valid @RequestBody CreateTrainerRequestDto requestDto){
        CreateTrainerResponseDto responseDto = gymFacade.createTrainer(requestDto);
        URI location = URI.create("/api/v1/trainers/" + responseDto.username());
        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(
            summary = "Update trainer profile",
            description = """
        Updates the profile information for a given trainer.
        Requires a valid session ID in the `X-Session-Id` header.
        **Access:** Owner of the trainer account.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized must be the owner",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainer with specified username not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only DELETE is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> updateTrainer(@PathVariable("username") String username,@Valid @RequestBody UpdateTrainerRequestDto requestDto){
        if(!username.equals(requestDto.username())) {
            throw new BadRequestException("Path username does not match request body username");
        }
        authService.validateOwnerAuth(username);
        TrainerResponseDto responseDto = gymFacade.updateTrainer(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Get trainer by username",
            description = "Retrieves a trainer's details by username. Requires a valid session ID from a trainer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK - Trainer retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer or owner)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainer with specified username not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only GET is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable("username") String username){
        authService.validateTrainerAuth();
        TrainerResponseDto responseDto = gymFacade.findTrainerByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Update trainer active state",
            description = "Allows a trainer or the trainer owner to activate/deactivate a trainer account. Requires a valid session ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content - Active state updated successfully", content = @Content), // No body
            @ApiResponse(responseCode = "400", description = "Bad Request - invalid body or missing field",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer or owner)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainer with specified username not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only PATCH is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{username}/state")
    public ResponseEntity<Map<String, String>> updateTrainerActiveState(@PathVariable("username") String username, @RequestBody ChangeStateRequestDto requestDto){
        authService.validateTrainerAuth();
        Boolean isActive = requestDto.isActive();
        gymFacade.updateTrainerActiveState(username, isActive);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Get trainer trainings with optional filters",
            description = """
        Retrieves the list of trainings for a given trainer.
        You can filter results by date range or trainee username
        Requires a valid session ID in the `X-Session-Id` header.
        **Access:** Owner of the trainer or any trainer.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainerTrainingListResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Trainer or owner validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainer with specified username not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only GET is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TrainerTrainingListResponseDto>> getTrainerTrainingsWithFilters(@PathVariable("username") String trainerUsername,
                                                                                               @RequestParam(value="fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                               @RequestParam(value="toDate", required = false) LocalDate toDate,
                                                                                               @RequestParam(value="traineeUsername",required = false) String traineeUsername){
        authService.validateTrainerAuth();
        TrainerTrainingListRequestDto requestDto = new TrainerTrainingListRequestDto(trainerUsername, fromDate, toDate, traineeUsername);
        return ResponseEntity.ok(gymFacade.getTrainerTrainings(requestDto));
    }

}
