package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.ProductDTO;
import com.hepl.backendapi.dto.post.ProductCreateDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Product management")
@RequestMapping("/v1")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/product/id/{id}")
    public ResponseEntity<ProductDTO> fetchProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Get products by list of IDs (POST)")
    @ApiResponse(responseCode = "200", description = "Products found")
    @ApiResponse(responseCode = "404", description = "Some resources not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/products/ids")
    public ResponseEntity<List<ProductDTO>> fetchProductsByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(productService.getProductsByIds(ids));
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Argument Not Valid or Quantity Out of Range",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        ProductDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Upload an image for a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file or request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    //@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping(path = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile) {

        String imageUrl = productService.createProductImage(id, imageFile);
        return ResponseEntity.ok(imageUrl);
    }


    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
