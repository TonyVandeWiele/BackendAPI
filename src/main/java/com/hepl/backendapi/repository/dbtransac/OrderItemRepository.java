package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import com.hepl.backendapi.utils.compositekey.OrderItemId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId> {
    List<OrderItemEntity> findAllByIdOrderId(Long orderId);

    List<OrderItemEntity> findAllByIdOrderIdIn(List<Long> orderIds);
    @Transactional
    @Modifying
    @Query("DELETE FROM OrderItemEntity oi WHERE oi.id.orderId = :orderId")
    void deleteAllById_OrderId(Long orderId);
}