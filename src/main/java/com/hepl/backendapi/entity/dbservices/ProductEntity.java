package com.hepl.backendapi.entity.dbservices;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@Table(name = "produits")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nom")
        private String name;

        @Column(name = "prix")
        private Double price;

        @Column(name = "description")
        private String description;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "categorie_id")
        private CategoryEntity category;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "stock_id")
        private StockEntity stock;
}
