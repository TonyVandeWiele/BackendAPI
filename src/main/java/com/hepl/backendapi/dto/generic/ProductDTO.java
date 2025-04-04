package com.hepl.backendapi.dto.generic;


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
}
