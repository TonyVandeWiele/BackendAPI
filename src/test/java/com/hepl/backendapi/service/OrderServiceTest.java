package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.OrderDTO;
import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.dto.post.OrderCreateDTO;
import com.hepl.backendapi.dto.post.OrderItemCreateDTO;
import com.hepl.backendapi.entity.dbtransac.*;
import com.hepl.backendapi.exception.MissingFieldException;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.repository.dbservices.TrackingRepository;
import com.hepl.backendapi.utils.enumeration.StatusEnum;
import com.hepl.backendapi.mappers.OrderMapper;
import com.hepl.backendapi.mappers.OrderItemMapper;
import com.hepl.backendapi.repository.dbtransac.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
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
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private TrackingRepository trackingRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler l'utilisateur connecté pour les autres tests
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("testUser")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("testUser", null, Collections.emptyList());
        authentication.setDetails(userDTO); // Définir UserDTO dans details
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testCreateOrder_Success() {
        // Configurer l'authentification pour ce test
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .name("testUser")
                .build();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("testUser", null, Collections.emptyList());
        authentication.setDetails(userDTO); // Définir UserDTO dans details
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AddressCreateDTO newAddress = AddressCreateDTO.builder()
                .street("123 Rue Exemple")
                .city("Paris")
                .zipCode("75000")
                .country("France")
                .build();

        OrderCreateDTO orderCreateDTO = OrderCreateDTO.builder()
                .orderItemsCreateDTOList(Collections.singletonList(OrderItemCreateDTO.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()))
                .newAddress(newAddress)
                .build();

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setPrice(20.0);

        StockEntity stockEntity = new StockEntity();
        stockEntity.setQuantity(10);
        stockEntity.setStockMin(5);
        stockEntity.setStockMax(100);

        when(productRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.singletonList(productEntity));
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stockEntity));

        when(orderItemRepository.save(any(OrderItemEntity.class))).thenReturn(new OrderItemEntity());
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(new OrderEntity());
        when(orderMapper.toDTO(any(OrderEntity.class))).thenReturn(new OrderDTO());

        AddressEntity addressEntity = new AddressEntity();
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                null, "123 Rue Exemple", "Paris", "75000", "France")).thenReturn(Optional.empty());
        when(addressRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        OrderDTO result = orderService.createOrder(orderCreateDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(OrderEntity.class));
        verify(orderItemRepository, times(1)).save(any(OrderItemEntity.class));
    }

    @Test
    public void testCreateOrder_MissingOrderItems() {
        OrderCreateDTO orderCreateDTO = OrderCreateDTO.builder()
                .orderItemsCreateDTOList(null)
                .build();

        MissingFieldException exception = assertThrows(MissingFieldException.class, () -> {
            orderService.createOrder(orderCreateDTO);
        });

        assertEquals("Missing required field: orderItemsCreateDTOList", exception.getMessage());
    }

    @Test
    public void testGetAllOrders() {
        List<OrderEntity> orderEntities = Collections.singletonList(new OrderEntity());
        when(orderRepository.findAll()).thenReturn(orderEntities);
        when(orderMapper.toDTO(any(OrderEntity.class))).thenReturn(new OrderDTO());

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    public void testUpdateOrderStatus_Success() {
        Long orderId = 1L;
        StatusEnum newStatus = StatusEnum.cancelled;

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setStatus(StatusEnum.pending);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1)
                .status(StatusEnum.cancelled)
                .build();
        when(orderMapper.toDTO(orderEntity)).thenReturn(orderDTO);

        OrderDTO updatedOrderDTO = orderService.updateOrderStatus(orderId, newStatus);

        assertNotNull(updatedOrderDTO);
        assertEquals(newStatus, updatedOrderDTO.getStatus());
        verify(orderRepository).save(orderEntity);
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() {
        Long orderId = 1L;
        StatusEnum newStatus = StatusEnum.cancelled;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RessourceNotFoundException exception = assertThrows(RessourceNotFoundException.class, () -> {
            orderService.updateOrderStatus(orderId, newStatus);
        });

        assertEquals("Ressource(s) (OrderEntity) Not Found for : Order ID not found: " + orderId, exception.getMessage());
    }
}