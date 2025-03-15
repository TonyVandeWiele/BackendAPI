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
    private Long produitId;
    private int quantity;
    private int stockMin;
    private int stockMax;
    private LocalDateTime lastUpdated;
}
