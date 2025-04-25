package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByClientId(Long clientId);

    List<OrderEntity> findAllByClientId(Long clientId);

    List<OrderEntity> findAllByDeliveryAgentId(Long deliveryAgentId);
}
