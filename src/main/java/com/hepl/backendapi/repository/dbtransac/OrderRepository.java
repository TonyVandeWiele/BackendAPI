package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
