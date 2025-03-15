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
    private int quantity;

    @Column(name = "stock_min")
    private int minStock;

    @Column(name = "stock_maj")
    private int maxStock;

    @Column(name = "date_maj")
    private LocalDateTime lastUpdated;
}
