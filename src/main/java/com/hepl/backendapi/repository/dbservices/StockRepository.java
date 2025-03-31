package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByProductId(Long productId);
}
