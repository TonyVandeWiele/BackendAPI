package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.UserCreateDTO;
import com.hepl.backendapi.dto.update.UserUpdateDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User management")
@RequestMapping("/v1")
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Argument Not Valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "400", description = "Argument Not Valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {

        UserDTO updatedUser = userService.updateUserCreateDTO(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
