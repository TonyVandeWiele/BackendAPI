package com.hepl.backendapi.dto.post;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDTO {
    private Long productId;
    private Integer quantity;
}
