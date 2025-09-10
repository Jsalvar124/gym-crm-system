package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.request.ChangeStateRequestDto;
import com.jsalva.gymsystem.dto.request.CreateTraineeRequestDto;
import com.jsalva.gymsystem.dto.request.TraineeTrainingListRequestDto;
import com.jsalva.gymsystem.dto.request.UpdateTraineeRequestDto;
import com.jsalva.gymsystem.dto.response.*;
import com.jsalva.gymsystem.entity.TrainingTypeEnum;
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
@RequestMapping("/trainees")
public class TraineeController {
    private final GymFacade gymFacade;

    private final AuthService authService;



    public TraineeController(GymFacade gymFacade, AuthService authService) {
        this.gymFacade = gymFacade;
        this.authService = authService;
    }

    @Operation(
            summary = "Create a new trainee",
            description = "Creates a new trainee with the provided details. "
                    + "The trainee's username is auto-generated. "
                    + "Returns the created trainee data and sets the `Location` header "
                    + "to the new resource's URI."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateTraineeResponseDto.class))),
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
    public ResponseEntity<CreateTraineeResponseDto> createTrainee(@Valid @RequestBody CreateTraineeRequestDto requestDto){
        CreateTraineeResponseDto responseDto = gymFacade.createTrainee(requestDto);
        URI location = URI.create("/trainees/"+ responseDto.username());
        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(
            summary = "Update trainee profile",
            description = """
        Updates the profile information for a given trainee.
        Requires a valid session ID in the `X-Session-Id` header.
        **Access:** Owner of the trainee account.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainee with specified username not found",
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
    public ResponseEntity<TraineeResponseDto> updateTrainee(@PathVariable("username") String username, @Valid @RequestBody UpdateTraineeRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        if(!username.equals(requestDto.username())) {
            throw new BadRequestException("Path username does not match request body username");
        }
        authService.validateOwnerAuth(sessionId, username);
        TraineeResponseDto responseDto = gymFacade.updateTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(
            summary = "Get trainee by username",
            description = "Retrieves a trainee's details by username. Requires a valid session ID from either a trainer or the trainee owner."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK - Trainee retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer or owner)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainee with specified username not found",
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
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerOrOwnerAuth(sessionId, username);
        TraineeResponseDto responseDto = gymFacade.findTraineeByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Delete trainee account",
            description = "Allows a trainer to permanently delete a trainee account. Requires a valid trainer session ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content - Trainee deleted successfully", content = @Content), // No body
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing session ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - caller not authorized (must be trainer)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainee with specified username not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only DELETE is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerAuth(sessionId); // Only a trainer can delete a trainee
        gymFacade.deleteTraineeByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update trainee active state",
            description = "Allows a trainer or the trainee owner to activate/deactivate a trainee account. Requires a valid session ID."
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
            @ApiResponse(responseCode = "404", description = "Not Found - trainee with specified username not found",
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
    public ResponseEntity<Map<String, String>> updateTraineeActiveState(@PathVariable("username") String username, @RequestBody ChangeStateRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerOrOwnerAuth(sessionId, username); // Only Trainers or the trainee owner can soft delete.
        Boolean isActive = requestDto.isActive();
        gymFacade.updateTraineeActiveState(username, isActive);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get trainee trainings with optional filters",
            description = """
        Retrieves the list of trainings for a given trainee.
        You can filter results by date range, trainer username, or training type.
        Requires a valid session ID in the `X-Session-Id` header.
        **Access:** Owner of the trainee or any trainer.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of trainings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TraineeTrainingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Trainer or owner validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - trainee with specified username not found",
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
    public ResponseEntity<List<TraineeTrainingListResponseDto>> getTraineeTrainingsWithFilters(@PathVariable("username") String traineeUsername,
                                                                                               @RequestParam(value="fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                               @RequestParam(value="toDate", required = false) LocalDate toDate,
                                                                                               @RequestParam(value="trainerUsername",required = false) String trainerUsername,
                                                                                               @RequestParam(value="trainingType", required = false) TrainingTypeEnum trainingType,
                                                                                               @RequestHeader("X-Session-Id") String sessionId) {

        authService.validateTrainerOrOwnerAuth(sessionId, traineeUsername);
        TraineeTrainingListRequestDto requestDto = new TraineeTrainingListRequestDto(traineeUsername, fromDate, toDate, trainerUsername, trainingType);
        return ResponseEntity.ok(gymFacade.getTraineeTrainings(requestDto));
    }


    @Operation(
            summary = "Get unassigned trainers for a trainee",
            description = """
        Retrieves the list of trainers that are **not yet assigned** to the given trainee.
        Requires a valid session ID in the `X-Session-Id` header.
        **Access:** Owner of the trainee or any trainer.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of unassigned trainers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainerSummaryDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Trainer or owner validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only GET is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerSummaryDto>> getUnassignedTrainers(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerOrOwnerAuth(sessionId, username);
        return ResponseEntity.ok(gymFacade.findUnassignedTrainersByTrainee(username));
    }


}
