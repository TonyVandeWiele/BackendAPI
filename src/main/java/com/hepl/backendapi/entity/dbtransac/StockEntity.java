package com.hepl.backendapi.entity.dbtransac;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stock")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private Integer quantity;

    @Column(name = "min_stock")
    private Integer stockMin;

    @Column(name = "max_stock")
    private Integer stockMax;

    @Column(name = "update_date")
    private LocalDateTime lastUpdated;

}
