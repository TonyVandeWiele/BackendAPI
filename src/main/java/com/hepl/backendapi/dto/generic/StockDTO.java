package com.hepl.backendapi.dto.generic;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Integer stockMin;
    private Integer stockMax;
    private LocalDateTime lastUpdated;
}
