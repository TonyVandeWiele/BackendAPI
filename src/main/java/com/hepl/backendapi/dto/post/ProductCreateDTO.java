package com.hepl.backendapi.dto.post;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    private String name;
    private Double price;
    private String description;
    private Long categoryId;
    private Integer quantity;
    private Integer stockMax;
    private Integer stockMin;
}

