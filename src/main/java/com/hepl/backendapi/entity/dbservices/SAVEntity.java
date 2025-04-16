package com.hepl.backendapi.entity.dbservices;

import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sav", schema = "bdservices")
public class SAVEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "order_id")
    private Long orderId;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestTypeEnum requestType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

