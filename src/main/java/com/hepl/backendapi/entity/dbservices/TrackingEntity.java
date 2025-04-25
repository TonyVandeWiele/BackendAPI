package com.hepl.backendapi.entity.dbservices;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_tracking", schema = "bdservices")
public class TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "estimate_delivery_date")
    private LocalDateTime estimateDeliveryDate;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
