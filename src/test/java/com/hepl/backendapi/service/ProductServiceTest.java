package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.ProductDTO;
import com.hepl.backendapi.entity.dbservices.CategoryEntity;
import com.hepl.backendapi.entity.dbservices.ProductEntity;
import com.hepl.backendapi.entity.dbservices.StockEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.ProductMapper;
import com.hepl.backendapi.repository.dbservices.CategoryRepository;
import com.hepl.backendapi.repository.dbservices.ProductRepository;
import com.hepl.backendapi.repository.dbservices.StockRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductEntity product1;
    private ProductEntity product2;
    private ProductDTO productDTO1;
    private ProductDTO productDTO2;
    private CategoryEntity category;
    private StockEntity stock;

    @BeforeEach
    void setUp() {
        product1 = new ProductEntity();
        product2 = new ProductEntity();
        
        productDTO1 = new ProductDTO();
        productDTO2 = new ProductDTO();

        category = new CategoryEntity();
        stock = new StockEntity();
    }

    @Test
    void testGetAllProducts_Success() {
        List<ProductEntity> productEntities = Arrays.asList(product1, product2);
        List<ProductDTO> productDTOs = Arrays.asList(productDTO1, productDTO2);

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.toDTOList(productEntities)).thenReturn(productDTOs);

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDTOList(productEntities);
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        when(productMapper.toDTO(product1)).thenReturn(productDTO1);

        ProductDTO result = productService.getProductById(productId);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).toDTO(product1);
    }

    @Test
    void testCreateProduct_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(1L);
        productDTO.setStockId(2L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(stock));

        ProductEntity productEntity = new ProductEntity();
        when(productMapper.toEntity(productDTO)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toDTO(productEntity)).thenReturn(productDTO1);

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(productEntity);
        verify(categoryRepository, times(1)).findById(1L);
        verify(stockRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> productService.createProduct(productDTO));
    }

    @Test
    void testCreateProduct_StockNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(1L);
        productDTO.setStockId(2L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(stockRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> productService.createProduct(productDTO));
    }

    @Test
    void testDeleteProductById_Success() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductById_NotFound() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(RessourceNotFoundException.class, () -> productService.deleteProductById(productId));
    }
}
