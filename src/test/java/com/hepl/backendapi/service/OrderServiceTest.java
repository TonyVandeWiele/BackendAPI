package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.OrderDTO;
import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.OrderMapper;
import com.hepl.backendapi.repository.dbtransac.OrderLinesRepository;
import com.hepl.backendapi.repository.dbtransac.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLinesRepository orderLinesRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderEntity order1;
    private OrderEntity order2;
    private OrderDTO orderDTO1;
    private OrderDTO orderDTO2;

    @BeforeEach
    void setup() {
        order1 = new OrderEntity();
        order2 = new OrderEntity();

        orderDTO1 = new OrderDTO();
        orderDTO2 = new OrderDTO();

    }

    @Test
    void testGetAllOrders_Success() {
        List<OrderEntity> orderEntities = Arrays.asList(order1, order2);
        List<OrderDTO> orderDTOs = Arrays.asList(orderDTO1, orderDTO2);

        when(orderRepository.findAll()).thenReturn(orderEntities);
        when(orderMapper.toDTOList(orderEntities)).thenReturn(orderDTOs);

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDTOList(orderEntities);
    }
    @Test
    void testGetOrderById_Success() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order1));
        when(orderMapper.toDTO(order1)).thenReturn(orderDTO1);

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toDTO(order1);
    }

}
