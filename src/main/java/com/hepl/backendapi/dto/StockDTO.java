package com.hepl.backendapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private Long id;
    private String name;
    private Long productId;
    private Integer quantity;
    private Integer stockMin;
    private Integer stockMax;
    private LocalDateTime lastUpdated;
}
