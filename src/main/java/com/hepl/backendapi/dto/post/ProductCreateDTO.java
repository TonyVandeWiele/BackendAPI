package com.hepl.backendapi.dto.post;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    @NotNull(message = "Product name cannot be empty")
    @NotBlank(message = "Product name cannot be empty")
    @Size(max = 255, message = "Product name must be at most 255 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotNull(message = "Stock max cannot be null")
    @Min(value = 1, message = "Stock max must be at least 1")
    private Integer stockMax;

    @NotNull(message = "Stock min cannot be null")
    @Min(value = 0, message = "Stock min must be at least 0")
    private Integer stockMin;
}


