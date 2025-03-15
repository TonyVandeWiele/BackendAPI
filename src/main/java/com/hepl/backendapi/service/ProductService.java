package com.hepl.backendapi.service;

import com.hepl.backendapi.RessourceNotFoundException;
import com.hepl.backendapi.dto.ProductDTO;
import com.hepl.backendapi.entity.CategoryEntity;
import com.hepl.backendapi.entity.ProductEntity;
import com.hepl.backendapi.entity.StockEntity;
import com.hepl.backendapi.mappers.ProductMapper;
import com.hepl.backendapi.repository.CategoryRepository;
import com.hepl.backendapi.repository.ProductRepository;
import com.hepl.backendapi.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
    }

    public List<ProductDTO> getAllProduct() {
        List<ProductEntity> productEntityList = productRepository.findAll();

        return productMapper.toDTOList(productEntityList);  // Utilisation du mapper injecté
    }

    public ProductDTO getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(ProductEntity.class.getSimpleName(), id));
        return productMapper.toDTO(productEntity);
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
