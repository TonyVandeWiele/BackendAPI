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

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderItemEntity oi WHERE oi.id.orderId = :orderId")
    void deleteAllById_OrderId(Long orderId);

    @Query("SELECT oi.id.productId FROM OrderItemEntity oi WHERE oi.id.orderId = :orderId")
    List<Long> findProductIdsByOrderId(Long orderId);
}