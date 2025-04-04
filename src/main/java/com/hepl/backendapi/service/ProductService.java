package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.ProductDTO;
import com.hepl.backendapi.dto.post.ProductCreateDTO;
import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import com.hepl.backendapi.entity.dbtransac.ProductEntity;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.OrderItemMapper;
import com.hepl.backendapi.mappers.ProductMapper;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.dbtransac.CategoryRepository;
import com.hepl.backendapi.repository.dbtransac.ProductRepository;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import com.hepl.backendapi.repository.dbtransac.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;

    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;
    private final OrderItemRepository orderItemRepository;
    private final StockMapper stockMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, OrderItemMapper orderItemMapper, CategoryRepository categoryRepository, StockRepository stockRepository, OrderItemRepository orderItemRepository, StockMapper stockMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.orderItemMapper = orderItemMapper;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
        this.orderItemRepository = orderItemRepository;
        this.stockMapper = stockMapper;
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntityList = productRepository.findAll();

        return productMapper.toDTOList(productEntityList);  // Utilisation du mapper injecté
    }

    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id));
        return productMapper.toDTO(productEntity);
    }

    public List<ProductDTO> getAllProductsByCategoryName(String name) {
        List<ProductEntity> products = productRepository.findAllByCategoryName(name);
        return productMapper.toDTOList(products);
    }

    public List<ProductDTO> getAllProductsByOrderId(Long orderId) {
        // 1. On récupère les productId via la base dbtransac
        List<OrderItemEntity> orderLines = orderItemRepository.findAllByIdOrderId(orderId);

        List<Long> productIds = orderLines.stream()
                .map(line -> line.getId().getProductId())
                .toList();

        // 3. Cherche les produits dans la base dbservices
        List<ProductEntity> products = productRepository.findAllById(productIds);

        // 4. Mapper en DTO
        return products.stream()
                .map(productMapper::toDTO)
                .toList();

    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductDTO createProduct(ProductCreateDTO productCreateDTO) {

        // Check if category exist
        CategoryEntity category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new RessourceNotFoundException(CategoryEntity.class.getSimpleName(), productCreateDTO.getCategoryId()));

        // Check quantity constraints
        if (productCreateDTO.getQuantity() < productCreateDTO.getStockMin() ||
                productCreateDTO.getQuantity() > productCreateDTO.getStockMax()) {
            throw new IllegalArgumentException("Quantity must be between stockMin and stockMax.");
        }

        ProductEntity product = ProductEntity.builder()
                .category(category)
                .description(productCreateDTO.getDescription())
                .price(productCreateDTO.getPrice())
                .name(productCreateDTO.getName())
                .build();
        ProductEntity productSaved = productRepository.save(product);

        StockEntity stock = StockEntity.builder().
                stockMax(productCreateDTO.getStockMax()).
                stockMin(productCreateDTO.getStockMin()).
                quantity(productCreateDTO.getQuantity()).
                lastUpdated(LocalDateTime.now()).
                build();

        stock.setProductId(productSaved.getId());
        stockRepository.save(stock);

        return productMapper.toDTO(productSaved);
    }

}
