package com.hepl.backendapi.dto.generic;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingDTO {
    private Long id;
    private Long orderId;
    private String trackingNumber;
    private LocalDateTime shipmentDate;
    private LocalDateTime estimateDeliveryDate;
    private Long addressId;
}
