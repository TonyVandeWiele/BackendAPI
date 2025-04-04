package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Stock management")
@RequestMapping("/v1")
public class StockController {

    StockService stockService;

    StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Get stock by Product ID")
    @ApiResponse(responseCode = "200", description = "Stock found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/stock/{productId}")
    public ResponseEntity<StockDTO> fetchStockById(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @Operation(summary = "Get all stocks")
    @ApiResponse(responseCode = "200", description = "Stocks retrieved successfully")
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> fetchAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @Operation(summary = "Update the stock of a product")
    @ApiResponse(responseCode = "200", description = "Stock quantity updated succefully")
    @ApiResponse(responseCode = "400", description = "The quantity is outside the permitted range")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/stock/{productId}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long productId, @RequestBody Integer quantity) {
        StockDTO updatedStock = stockService.updateStock(productId, quantity);
        return ResponseEntity.ok(updatedStock);
    }
}
