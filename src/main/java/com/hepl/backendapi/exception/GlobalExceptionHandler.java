package com.hepl.backendapi.exception;

import com.hepl.backendapi.utils.enumeration.StatusEnum;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            validationEx.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
        } else if (ex instanceof ConstraintViolationException constraintEx) {
            errors.put("error", constraintEx.getMessage()); // Utilise juste le message
        }

        return buildErrorResponse("Validation error: " + errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRessourceNotFound(RessourceNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateProductIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateProductIdException(DuplicateProductIdException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    @ApiResponse(
            responseCode = "503",
            description = "Database access error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        return buildErrorResponse("An error occurred while accessing the database. Please try again later." + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(SQLException.class)
    @ApiResponse(
            responseCode = "503",
            description = "SQL Exception: Database connection issue",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        return buildErrorResponse("Database connection error. Please check your database status.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(PersistenceException.class)
    @ApiResponse(
            responseCode = "503",
            description = "Persistence error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException ex) {
        return buildErrorResponse(String.valueOf(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(JpaSystemException.class)
    @ApiResponse(
            responseCode = "503",
            description = "JPA system error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleJpaSystemException(JpaSystemException ex) {
        return buildErrorResponse(String.valueOf(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MissingFieldException.class)
    @ApiResponse(
            responseCode = "400",
            description = "Missing required field in request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleMissingFieldException(MissingFieldException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleEnumConversionError(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(StatusEnum.class)) {
            String paramName = ex.getName();
            String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
            String message = "Invalid value for parameter '" + paramName + "': '" + invalidValue
                    + "'. Allowed values: " + Arrays.toString(StatusEnum.values());
            return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
        }

        // Si ce n'est pas un problème avec StatusEnum, renvoyer une erreur générique
        return  buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Méthode pour éviter la duplication de code
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), message, status.value());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
