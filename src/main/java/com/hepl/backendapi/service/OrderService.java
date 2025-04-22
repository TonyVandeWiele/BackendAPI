package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.OrderDTO;
import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.dto.post.OrderCreateDTO;
import com.hepl.backendapi.dto.post.OrderItemCreateDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.entity.dbtransac.ProductEntity;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import com.hepl.backendapi.entity.dbservices.TrackingEntity;
import com.hepl.backendapi.exception.DuplicateProductIdException;
import com.hepl.backendapi.exception.MissingFieldException;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.*;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import com.hepl.backendapi.repository.dbtransac.ProductRepository;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import com.hepl.backendapi.repository.dbtransac.OrderItemRepository;
import com.hepl.backendapi.repository.dbtransac.OrderRepository;
import com.hepl.backendapi.repository.dbservices.TrackingRepository;
import com.hepl.backendapi.utils.UtilsClass;
import com.hepl.backendapi.utils.compositekey.OrderItemId;
import com.hepl.backendapi.utils.enumeration.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    ProductRepository productRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    ProductMapper productMapper;
    TrackingRepository trackingRepository;
    AddressRepository addressRepository;
    TrackingMapper trackingMapper;
    StockRepository stockRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper, ProductRepository productRepository, ProductMapper productMapper, AddressRepository addressRepository, TrackingRepository trackingRepository , AddressMapper addressMapper, TrackingMapper trackingMapper, StockRepository stockRepository, LocalContainerEntityManagerFactoryBean entityManagerFactory2, LocalContainerEntityManagerFactoryBean dbservicesEntityManagerFactory) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.addressRepository = addressRepository;
        this.trackingRepository = trackingRepository;
        this.trackingMapper = trackingMapper;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();

        // 1. Extraire les IDs des commandes
        List<Long> orderIds = orderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        // 2. Récupérer tous les OrderItems associés à ces commandes
        List<OrderItemEntity> allOrderItems = orderItemRepository.findAllByIdOrderIdIn(orderIds);

        // 3. Grouper les OrderItems par commande
        Map<Long, List<OrderItemEntity>> itemsByOrderId = allOrderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getId().getOrderId()));

        // 4. Récupérer tous les trackingIds (non null)
        List<Long> trackingIds = orderEntities.stream()
                .map(OrderEntity::getTrackingId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 5. Récupérer tous les TrackingEntity
        List<TrackingEntity> trackingEntities = trackingRepository.findAllById(trackingIds);
        Map<Long, TrackingEntity> trackingById = trackingEntities.stream()
                .collect(Collectors.toMap(TrackingEntity::getId, t -> t));

        // 6. Construire les DTO enrichis
        return orderEntities.stream().map(orderEntity -> {
            OrderDTO dto = orderMapper.toDTO(orderEntity);

            List<OrderItemEntity> itemEntities = itemsByOrderId.getOrDefault(orderEntity.getId(), Collections.emptyList());
            dto.setOrderItems(orderItemMapper.toDTOList(itemEntities));

            if (orderEntity.getTrackingId() != null) {
                TrackingEntity trackingEntity = trackingById.get(orderEntity.getTrackingId());
                if (trackingEntity != null) {
                    dto.setTracking(trackingMapper.toTrackingDTO(trackingEntity));
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }



    public OrderDTO getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id));

        OrderDTO orderDTO = orderMapper.toDTO(orderEntity);

        // Récupération des produits associés
        List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllByIdOrderId(id);
        orderDTO.setOrderItems(orderItemMapper.toDTOList(orderItemEntityList));

        if(orderEntity.getTrackingId() != null) {
            // Récupération du tracking associé
            TrackingEntity trackingEntity = trackingRepository.findById(orderEntity.getTrackingId()).orElseThrow(() -> new RessourceNotFoundException(TrackingEntity.class.getSimpleName(), orderEntity.getTrackingId()));;
            orderDTO.setTracking(trackingMapper.toTrackingDTO(trackingEntity));
        }
        return orderDTO;
    }

    @Transactional
    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {

        if (orderCreateDTO.getOrderItemsCreateDTOList() == null || orderCreateDTO.getOrderItemsCreateDTOList().isEmpty()) {
            throw new MissingFieldException("orderItemsCreateDTOList");
        }

        List<Long> productIds = orderCreateDTO.getOrderItemsCreateDTOList().stream()
                .map(OrderItemCreateDTO::getProductId)
                .toList();

        // Vérif doublons
        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicateIds = productIds.stream()
                .filter(id -> !uniqueIds.add(id))
                .collect(Collectors.toSet());

        if (!duplicateIds.isEmpty()) {
            throw new DuplicateProductIdException("Duplicate product IDs found: " + duplicateIds);
        }

        List<ProductEntity> productEntities = productRepository.findAllById(productIds);

        if (productEntities.size() != productIds.size()) {
            throw new RessourceNotFoundException(ProductEntity.class.getSimpleName(), "Produit IDs not found in: " + productIds);
        }

        // --- Résolution de l'adresse ---
        AddressEntity addressEntity = resolveAddress(orderCreateDTO);

        // Map produit → prix
        Map<Long, Double> productPrices = productEntities.stream()
                .collect(Collectors.toMap(ProductEntity::getId, ProductEntity::getPrice));

        // Vérification des stocks et prépa des entités
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        List<StockEntity> stockEntitiesToUpdate = new ArrayList<>();
        double total = 0.0;

        for (OrderItemCreateDTO itemDTO : orderCreateDTO.getOrderItemsCreateDTOList()) {

            StockEntity stockEntity = stockRepository.findByProductId(itemDTO.getProductId())
                    .orElseThrow(() -> new RessourceNotFoundException("This product does not have a stock", itemDTO.getProductId()));

            int newQuantity = stockEntity.getQuantity() - itemDTO.getQuantity();
            UtilsClass.validateQuantityInRange(newQuantity, stockEntity);

            stockEntity.setQuantity(newQuantity);
            stockEntitiesToUpdate.add(stockEntity);

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setId(new OrderItemId(null, itemDTO.getProductId())); // On settra l'orderId après
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItemEntities.add(orderItem);

            Double price = productPrices.get(itemDTO.getProductId());
            total += price != null ? price * itemDTO.getQuantity() : 0.0;
        }

        // --- Création de la commande ---
        OrderEntity orderEntity = OrderEntity.builder()
                .orderDate(LocalDate.now())
                .orderTime(LocalTime.now())
                .status(StatusEnum.pending)
                .total(total)
                .trackingId(null)
                .bankName("Unknown")
                .address(addressEntity)
                .clientId(1L)
                .build();

        orderRepository.save(orderEntity);

        // Lier les items à la commande et sauvegarder
        for (OrderItemEntity item : orderItemEntities) {
            item.setId(new OrderItemId(orderEntity.getId(), item.getId().getProductId()));
            orderItemRepository.save(item);
        }

        // Mise à jour des stocks
        stockRepository.saveAll(stockEntitiesToUpdate);

        OrderDTO orderDTO = orderMapper.toDTO(orderEntity);
        orderDTO.setOrderItems(orderItemMapper.toDTOList(orderItemEntities));
        return orderDTO;
    }

    private AddressEntity resolveAddress(OrderCreateDTO orderCreateDTO) {
        if (orderCreateDTO.getAdresseId() != null) {
            return addressRepository.findById(orderCreateDTO.getAdresseId())
                    .orElseThrow(() -> new RessourceNotFoundException("AddressEntity", "Address ID not found: " + orderCreateDTO.getAdresseId()));
        }

        if (orderCreateDTO.getNewAddress() != null) {
            AddressCreateDTO newAddressDTO = orderCreateDTO.getNewAddress();
            return addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                    newAddressDTO.getNumber(),
                    newAddressDTO.getStreet(),
                    newAddressDTO.getCity(),
                    newAddressDTO.getZipCode(),
                    newAddressDTO.getCountry()
            ).orElseGet(() -> {
                AddressEntity address = AddressEntity.builder()
                        .number(newAddressDTO.getNumber())
                        .street(newAddressDTO.getStreet())
                        .zipCode(newAddressDTO.getZipCode())
                        .country(newAddressDTO.getCountry())
                        .city(newAddressDTO.getCity())
                        .build();
                return addressRepository.save(address);
            });
        }

        throw new MissingFieldException("adresseId or newAddress");
    }

    public OrderDTO updateOrderStatus(Long orderId, StatusEnum newStatus) {
        // Vérifier si la commande existe
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RessourceNotFoundException(OrderEntity.class.getSimpleName(), "Order ID not found: " + orderId));

        // Mettre à jour le statut de la commande
        orderEntity.setStatus(newStatus);

        // Si la commande passe en annulée, remettre les produits en stock
        if (newStatus == StatusEnum.cancelled) {
            List<OrderItemEntity> orderItems = orderItemRepository.findAllByIdOrderId(orderId);

            for (OrderItemEntity item : orderItems) {
                StockEntity stockEntity = stockRepository.findByProductId(item.getId().getProductId()).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), "Stock ID not found: " + item.getId().getProductId()));

                int newQuantity = stockEntity.getQuantity() - item.getQuantity();
                UtilsClass.validateQuantityInRange(newQuantity, stockEntity);

                stockEntity.setQuantity(newQuantity);

                // Remettre la quantité en stock
                stockRepository.save(stockEntity); // Sauvegarder les modifications de stock
            }
        }

        // Créer un Tracking si le statut est "confirmed" et qu'il n'existe pas déjà
        if (newStatus == StatusEnum.confirmed && orderEntity.getTrackingId() == null) {
            TrackingEntity trackingEntity = TrackingEntity.builder()
                    .orderId(orderEntity.getId())
                    .trackingNumber(generateTrackingId())
                    .estimateDeliveryDate(LocalDateTime.now().plusDays(3))
                    .shipmentDate(LocalDateTime.now().plusDays(1))
                    .addressId(orderEntity.getAddress().getId())
                    .build();

            trackingRepository.save(trackingEntity); // Sauvegarder le tracking dans la base
            orderEntity.setTrackingId(trackingEntity.getId()); // Lier le tracking à la commande
        }

        // Sauvegarder la commande avec le nouveau statut et éventuellement le tracking associé
        orderRepository.save(orderEntity);

        OrderDTO updatedOrderDTO = orderMapper.toDTO(orderEntity);

        // Si un TrackingEntity a été créé ou existe déjà, le mettre dans le DTO
        if (orderEntity.getTrackingId() != null) {
            TrackingEntity trackingEntity = trackingRepository.findById(orderEntity.getTrackingId())
                    .orElseThrow(() -> new RessourceNotFoundException(TrackingEntity.class.getSimpleName(), "Tracking ID not found: " + orderEntity.getTrackingId()));

            updatedOrderDTO.setTracking(trackingMapper.toTrackingDTO(trackingEntity)); // Ajouter le DTO de tracking
        }

        return updatedOrderDTO;
    }


    @Transactional
    public void deleteOrder(Long orderId) {
        // Vérifier si la commande existe
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new RessourceNotFoundException(OrderEntity.class.getSimpleName(), orderId));

        // Supprimer le tracking associé
        trackingRepository.deleteById(orderEntity.getTrackingId());

        // Supprimer les lignes de commande associées
        orderItemRepository.deleteAllById_OrderId(orderId);

        // Supprimer la commande
        orderRepository.delete(orderEntity);
    }

    private String generateTrackingId() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
