package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.OrderDTO;
import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import com.hepl.backendapi.entity.dbservices.ProductEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.OrderLinesMapper;
import com.hepl.backendapi.mappers.OrderMapper;
import com.hepl.backendapi.repository.dbtransac.OrderLinesRepository;
import com.hepl.backendapi.repository.dbtransac.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository;
    OrderLinesRepository orderLinesRepository;
    OrderMapper orderMapper;
    OrderLinesMapper orderLinesMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, OrderLinesRepository orderLinesRepository, OrderLinesMapper orderLinesMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderLinesRepository = orderLinesRepository;
        this.orderLinesMapper = orderLinesMapper;
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderMapper.toDTOList(orderEntities);
    }

    public OrderDTO getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id));
        return orderMapper.toDTO(orderEntity);
    }

}
