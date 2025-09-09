package com.jsalva.gymsystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response returned by all endpoints")
public record ErrorResponseDto(
        @Schema(description = "Short error code, usually the HTTP reason phrase", example = "Error Type")
        String error,

        @Schema(description = "Detailed error message", example = "Detailed Error message")
        String message,

        @Schema(description = "Timestamp when the error occurred", example = "2025-09-09T17:29:18.1170655")
        String timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status
) {
}
