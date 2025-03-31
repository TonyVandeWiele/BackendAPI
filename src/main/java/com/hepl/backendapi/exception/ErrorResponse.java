package com.hepl.backendapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error", example = "Example : 2025-03-28T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Error message", example = "Example of an error message")
    private String message;

    @Schema(description = "HTTP status code", example = "Example : (404)")
    private int status;

}
