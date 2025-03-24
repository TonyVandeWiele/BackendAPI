package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
}
