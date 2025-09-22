package com.jsalva.gymsystem.controller;

import com.jsalva.gymsystem.dto.response.ErrorResponseDto;
import com.jsalva.gymsystem.dto.response.TrainingTypeResponseDto;
import com.jsalva.gymsystem.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("training-types")
public class TrainingTypeController {
    GymFacade gymFacade;

    public TrainingTypeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Operation(
            summary = "Get all training types",
            description = "Retrieves the list of all available training types."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of training types retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TrainingTypeResponseDto.class))
                    )
            ),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed - only GET is supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDto>> getTrainingTypes(){
        List<TrainingTypeResponseDto> trainingTypes = gymFacade.getAllTrainingTypes();
        return ResponseEntity.ok(trainingTypes);
    }
}
