package com.hepl.backendapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nom")
    private String name;

    @Column(name = "produit_id")
    private Long productId;

    @Column(name = "quantité")
    private Integer quantity;

    @Column(name = "stock_min")
    private Integer stockMin;

    @Column(name = "stock_max")
    private Integer stockMax;

    @Column(name = "date_maj")
    private LocalDateTime lastUpdated;
}
