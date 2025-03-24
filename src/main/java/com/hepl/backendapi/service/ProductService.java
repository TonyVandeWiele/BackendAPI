package com.hepl.backendapi.service;

import com.hepl.backendapi.entity.dbservices.CategoryEntity;
import com.hepl.backendapi.entity.dbservices.ProductEntity;
import com.hepl.backendapi.entity.dbservices.StockEntity;
import com.hepl.backendapi.entity.dbtransac.OrderLinesEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.dto.ProductDTO;
import com.hepl.backendapi.mappers.OrderLinesMapper;
import com.hepl.backendapi.mappers.ProductMapper;
import com.hepl.backendapi.repository.dbservices.CategoryRepository;
import com.hepl.backendapi.repository.dbservices.ProductRepository;
import com.hepl.backendapi.repository.dbservices.StockRepository;
import com.hepl.backendapi.repository.dbtransac.OrderLinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderLinesMapper orderLinesMapper;

    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;
    private final OrderLinesRepository orderLinesRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, OrderLinesMapper orderLinesMapper, CategoryRepository categoryRepository, StockRepository stockRepository, OrderLinesRepository orderLinesRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.orderLinesMapper = orderLinesMapper;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
        this.orderLinesRepository = orderLinesRepository;

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
        List<OrderLinesEntity> orderLines = orderLinesRepository.findAllByIdOrderId(orderId);

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

    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity product = productMapper.toEntity(productDTO);

        // Associer les entités liées à partir des IDs
        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RessourceNotFoundException(CategoryEntity.class.getSimpleName(), productDTO.getCategoryId()));
        StockEntity stock = stockRepository.findById(productDTO.getStockId())
                .orElseThrow(() -> new RessourceNotFoundException(StockEntity.class.getSimpleName(), productDTO.getStockId()));

        product.setCategory(category);
        product.setStock(stock);

        ProductEntity saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

}
