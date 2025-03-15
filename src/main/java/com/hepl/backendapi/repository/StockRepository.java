package com.hepl.backendapi.repository;

import com.hepl.backendapi.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
}
