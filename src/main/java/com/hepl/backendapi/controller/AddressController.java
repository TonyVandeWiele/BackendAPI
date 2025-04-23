package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Address management")
@RequestMapping("/v1")
public class AddressController {
    final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Get address by id")
    @ApiResponse(responseCode = "200", description = "Address retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation =ErrorResponse.class)))
    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAllAddresses(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @Operation(summary = "Get all the address ids for a specific client")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping("/addresses/{clientId}")
    public ResponseEntity<List<AddressDTO>> getAllAddressesByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(addressService.getAddressesByClientId(clientId));
    }
}
