package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.CategoryDTO;
import com.hepl.backendapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Category management")
@RequestMapping("/v1")
public class CategoryController {
    CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> fetchAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Get category by ID")
    @ApiResponse(responseCode = "200", description = "Category found")
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> fetchCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
