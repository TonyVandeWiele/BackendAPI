package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.OrderDTO;
import com.hepl.backendapi.dto.generic.TrackingDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

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

    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderMapper.toDTOList(orderEntities);
    }


    public OrderDTO getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id));

        OrderDTO orderDTO = orderMapper.toDTO(orderEntity);

        // Récupération des produits associés
        List<OrderItemEntity> orderItemEntityList = orderItemRepository.findAllByIdOrderId(id);
        orderDTO.setOrderItems(orderItemMapper.toDTOList(orderItemEntityList));

        // Récupération du tracking associé
        TrackingEntity trackingEntity = trackingRepository.findById(orderEntity.getTrackingId()).orElseThrow(() -> new RessourceNotFoundException(TrackingEntity.class.getSimpleName(), orderEntity.getTrackingId()));;
        orderDTO.setTracking(trackingMapper.toTrackingDTO(trackingEntity));

        return orderDTO;
    }

    @Transactional
    public OrderDTO createOrder(OrderCreateDTO orderCreateDTO) {

        // If products are present in the order or not
        if (orderCreateDTO.getOrderItemsCreateDTOList() == null || orderCreateDTO.getOrderItemsCreateDTOList().isEmpty()) {
            throw new MissingFieldException("orderItemsCreateDTOList");
        }

        List<Long> productIds = new ArrayList<>();
        for (OrderItemCreateDTO orderItem : orderCreateDTO.getOrderItemsCreateDTOList()) {
            productIds.add(orderItem.getProductId());
        }

        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicateIds = productIds.stream()
                .filter(id -> !uniqueIds.add(id)) // Ajoute à uniqueIds et détecte les doublons
                .collect(Collectors.toSet());

        List<ProductEntity> productEntities = productRepository.findAllById(productIds);

        if (!duplicateIds.isEmpty()) {
            throw new DuplicateProductIdException("Duplicate product IDs found: " + duplicateIds);
        }

        // Vérifier si tous les IDs existent
        if (productEntities.size() != productIds.size()) {
            throw new RessourceNotFoundException(ProductEntity.class.getSimpleName(), "Produit IDs not found in: " + productIds);
        }

        AddressEntity addressEntity;

        if (orderCreateDTO.getAdresseId() != null) {
            // Utiliser une adresse existante
            addressEntity = addressRepository.findById(orderCreateDTO.getAdresseId())
                    .orElseThrow(() -> new RessourceNotFoundException("AddressEntity", "Address ID not found: " + orderCreateDTO.getAdresseId()));
        } else if (orderCreateDTO.getNewAddress() != null) {
            // Vérifier si l'adresse existe déjà
            AddressCreateDTO newAddressDTO = orderCreateDTO.getNewAddress();
            Optional<AddressEntity> existingAddress = addressRepository.findByStreetAndCityAndZipCodeAndCountry(
                    newAddressDTO.getStreet(), newAddressDTO.getCity(), newAddressDTO.getZipCode(), newAddressDTO.getCountry());

            if (existingAddress.isPresent()) {
                addressEntity = existingAddress.get();
            } else {
                // Créer une nouvelle adresse
                addressEntity = AddressEntity.builder()
                        .number(newAddressDTO.getNumber())
                        .street(newAddressDTO.getStreet())
                        .zipCode(newAddressDTO.getZipCode())
                        .country(newAddressDTO.getCountry())
                        .city(newAddressDTO.getCity())
                        .build();
                addressRepository.save(addressEntity);
            }
        } else {
            throw new MissingFieldException("adresseId or newAddress");
        }

        // Associer les produits à leur prix
        Map<Long, Double> productPrices = productEntities.stream()
                .collect(Collectors.toMap(ProductEntity::getId, ProductEntity::getPrice));

        // Calcul du total
        double total = orderCreateDTO.getOrderItemsCreateDTOList().stream()
                .mapToDouble(item -> {
                    Double price = productPrices.get(item.getProductId());
                    return price != null ? price * item.getQuantity() : 0.0;
                })
                .sum();

        // Création de la commande
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

        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        // Enregistrer chaque ligne de commande
        for (OrderItemCreateDTO orderItemDTO : orderCreateDTO.getOrderItemsCreateDTOList()) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setId(new OrderItemId(orderEntity.getId(), orderItemDTO.getProductId())); // Clé composite
            orderItemEntity.setQuantity(orderItemDTO.getQuantity());

            // Mise à jour du stock
            StockEntity stockEntity = stockRepository.findByProductId(orderItemDTO.getProductId())
                    .orElseThrow(() -> new RessourceNotFoundException("This product does not have a stock", orderItemDTO.getProductId()));

            // Calcul de la nouvelle quantité
            int quantity = stockEntity.getQuantity() - orderItemDTO.getQuantity();

            UtilsClass.validateQuantityInRange(quantity, stockEntity);  // Vérification de la quantité

            // Mise à jour du stock
            stockEntity.setQuantity(quantity);

            // Sauvegarde du stock mis à jour et de l'élément de commande
            stockRepository.save(stockEntity);
            orderItemRepository.save(orderItemEntity);


            orderItemEntityList.add(orderItemEntity);
        }

        OrderDTO orderDTO = orderMapper.toDTO(orderEntity);

        orderDTO.setOrderItems(orderItemMapper.toDTOList(orderItemEntityList));
        return orderDTO;
    }

    public OrderDTO updateOrderStatus(Long orderId, StatusEnum newStatus) {
        // Vérifier si la commande existe
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RessourceNotFoundException(OrderEntity.class.getSimpleName(), "Order ID not found: " + orderId));

        // Mettre à jour le statut de la commande
        orderEntity.setStatus(newStatus);

        // Créer un Tracking si le statut est "shipped" et qu'il n'existe pas déjà
        if (newStatus == StatusEnum.shipped && orderEntity.getTrackingId() == null) {
            TrackingEntity trackingEntity = TrackingEntity.builder()
                    .orderId(orderEntity.getId())
                    .trackingNumber(generateTrackingId())
                    .estimateDeliveryDate(LocalDateTime.now().plusDays(3))
                    .shipmentDate(LocalDateTime.now())
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
