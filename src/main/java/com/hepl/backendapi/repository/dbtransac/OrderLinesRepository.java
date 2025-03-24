package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.OrderLinesEntity;
import com.hepl.backendapi.utils.compositekey.OrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLinesRepository extends JpaRepository<OrderLinesEntity, OrderLineId> {
    List<OrderLinesEntity> findAllByIdOrderId(Long orderId);

}