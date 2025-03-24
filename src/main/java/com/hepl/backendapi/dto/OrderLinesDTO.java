package com.hepl.backendapi.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLinesDTO {

    private Long orderId;
    private Long productId;
    private Long quantity;
}
