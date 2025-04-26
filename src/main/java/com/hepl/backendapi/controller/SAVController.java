package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.SAVDTO;
import com.hepl.backendapi.dto.post.SAVCreateDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.SAVService;
import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "SAV management")
@RequestMapping("/v1")
public class SAVController {

    private final SAVService savService;

    public SAVController(SAVService savService) {
        this.savService = savService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Create an SAV request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SAV Request created successfully"),
            @ApiResponse(responseCode = "400", description = "Argument Not Valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PostMapping("/savticket")
    public ResponseEntity<SAVDTO> createSAVRequest(@Valid @RequestBody SAVCreateDTO savCreateDTO) {
        return ResponseEntity.ok(savService.createSAVRequest(savCreateDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all SAV requests")
    @ApiResponse(responseCode = "200", description = "SAV requests retrieved successfully")
    @GetMapping("/savtickets")
    public ResponseEntity<List<SAVDTO>> getAllSAVRequests() {
        return ResponseEntity.ok(savService.getAllSAVRequests());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update the status of a SAV ticket")
    @ApiResponse(responseCode = "200", description = "SAV ticket status updated successfully")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation =ErrorResponse.class)))
    @PutMapping("/savticket/{id}/status")
    public ResponseEntity<SAVDTO> updateSAVStatus(@PathVariable Long id, @RequestParam RequestTypeEnum status) {
        return ResponseEntity.ok(savService.updateSAVStatus(id, status));
    }
}

