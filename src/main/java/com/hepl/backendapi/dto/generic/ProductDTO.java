package com.hepl.backendapi.dto.generic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private CategoryDTO category;
    private StockDTO stock;
}
