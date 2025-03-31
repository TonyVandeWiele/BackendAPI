package com.hepl.backendapi.dto.generic;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long orderId;
    private Long productId;
    private Long quantity;
}
