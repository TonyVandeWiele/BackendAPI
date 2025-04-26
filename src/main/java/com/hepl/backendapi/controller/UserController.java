package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.UserCreateDTO;
import com.hepl.backendapi.dto.update.UserUpdateDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.UserService;
import com.hepl.backendapi.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "User management")
@RequestMapping("/v1")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current-time")
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> fetchAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get connected user info")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/me/user")
    public ResponseEntity<UserDTO> fetchConnectedUser() {
        Long userId = SecurityUtils.getCurrentUserDetails().getId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //@PreAuthorize("hasRole('AUTH_SERVER')")
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Argument Not Valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Update connected user info")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "400", description = "Argument Not Valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/me/user")
    public ResponseEntity<UserDTO> updateConnectedUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        Long userId = SecurityUtils.getCurrentUserDetails().getId();
        UserDTO updatedUser = userService.updateUserDTO(userId, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "400", description = "Argument Not Valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {

        UserDTO updatedUser = userService.updateUserDTO(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
