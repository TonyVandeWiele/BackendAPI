package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.ProductDTO;
import com.hepl.backendapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainApiController {

    private final ProductService productService;

    @Autowired
    public MainApiController(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping("/test")
    public String test() {
        return "Spring Boot Web Dependency Test Successful!";
    }
    @GetMapping("/")
    public String home() {
        return "Welcome! This is the home page.";
    }

    @GetMapping("/v1/product")
    public ResponseEntity<ProductDTO> fetchProductById(@RequestParam Long id) { return ResponseEntity.ok(productService.getProductById(id)); }

    @GetMapping("/v1/products")
    public ResponseEntity<List<ProductDTO>> fetchAllProducts() { return ResponseEntity.ok(productService.getAllProduct()); }

    @PostMapping("/v1/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
