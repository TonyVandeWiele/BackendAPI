package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get stock by ID")
    @ApiResponse(responseCode = "200", description = "Stock found")
    @GetMapping("/stock/{id}")
    public ResponseEntity<StockDTO> fetchStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @Operation(summary = "Get all stocks")
    @ApiResponse(responseCode = "200", description = "Stocks retrieved successfully")
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> fetchAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @Operation(summary = "Update the stock of a product")
    @ApiResponse(responseCode = "200", description = "Stock quantity updated succefully")
    @PutMapping("/stock/{productId}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long productId, @RequestBody Integer quantity) {
        StockDTO updatedStock = stockService.updateStock(productId, quantity);
        return ResponseEntity.ok(updatedStock);
    }
}
