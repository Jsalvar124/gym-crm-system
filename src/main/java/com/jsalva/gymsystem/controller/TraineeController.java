package com.jsalva.gymsystem.controller;

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

    @PostMapping
    public ResponseEntity<CreateTraineeResponseDto> createTrainee(@Valid @RequestBody CreateTraineeRequestDto requestDto){
        CreateTraineeResponseDto responseDto = gymFacade.createTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> updateTrainee(@PathVariable("username") String username, @Valid @RequestBody UpdateTraineeRequestDto requestDto, @RequestHeader("X-Session-Id") String sessionId){
        if(!username.equals(requestDto.username())) {
            throw new BadRequestException("Path username does not match request body username");
        }
        authService.validateOwnerAuth(sessionId, username);
        TraineeResponseDto responseDto = gymFacade.updateTrainee(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerOrOwnerAuth(sessionId, username);
        TraineeResponseDto responseDto = gymFacade.findTraineeByUsername(username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerAuth(sessionId); // Only a trainer can delete a trainee
        gymFacade.deleteTraineeByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/state")
    public ResponseEntity<Map<String, String>> updateTraineeActiveState(@PathVariable("username") String username, @RequestBody Map<String, Boolean> requestBody, @RequestHeader("X-Session-Id") String sessionId){
        authService.validateTrainerOrOwnerAuth(sessionId, username); // Only Trainers or the trainee owner can soft delete.
        Boolean isActive = requestBody.get("isActive");
        gymFacade.updateTraineeActiveState(username, isActive);
        return ResponseEntity.ok().build();
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
