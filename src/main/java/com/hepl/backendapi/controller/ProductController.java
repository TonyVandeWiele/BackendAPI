package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.ProductDTO;
import com.hepl.backendapi.dto.post.ProductCreateDTO;
import com.hepl.backendapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Product management")
@RequestMapping("/v1")
public class ProductController {

    ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @GetMapping("/product/id/{id}")
    public ResponseEntity<ProductDTO> fetchProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Get all products by category name")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/products/category/{categoryName}")
    public ResponseEntity<List<ProductDTO>> fetchAllProductsByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getAllProductsByCategoryName(categoryName));
    }

    @Operation(summary = "Get products of an order by orderId")
    @ApiResponse(responseCode = "200", description = "Products found")
    @GetMapping("/order/{orderId}/products")
    public ResponseEntity<List<ProductDTO>> fetchAllProductsByOrderId(@PathVariable Long orderId) {
        List<ProductDTO> products = productService.getAllProductsByOrderId(orderId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "All products retrieved successfully")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> fetchAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
