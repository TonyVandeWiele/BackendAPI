package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.ProductDTO;
import com.hepl.backendapi.dto.post.ProductCreateDTO;
import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import com.hepl.backendapi.entity.dbtransac.ProductEntity;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.ProductMapper;
import com.hepl.backendapi.repository.dbtransac.CategoryRepository;
import com.hepl.backendapi.repository.dbtransac.ProductRepository;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import com.hepl.backendapi.repository.dbtransac.OrderItemRepository;
import com.hepl.backendapi.utils.compositekey.OrderItemId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ProductService productService;

    private ProductEntity productEntity;
    private ProductDTO productDTO;
    private CategoryEntity categoryEntity;
    private ProductCreateDTO productCreateDTO;
    private StockEntity stockEntity;
    private OrderItemEntity orderItemEntity;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Configuration de CategoryEntity
        categoryEntity = CategoryEntity.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic products")
                .build();

        // Configuration de ProductEntity
        productEntity = ProductEntity.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .price(999.99)
                .category(categoryEntity)
                .imageUrl("/images/products/laptop.jpg")
                .build();

        // Configuration de ProductDTO
        productDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("High-end laptop")
                .price(999.99)
                .category(null) // Le champ category n'est pas utilisé dans les tests pour simplifier
                .imageUrl("/images/products/laptop.jpg")
                .build();

        // Configuration de ProductCreateDTO
        productCreateDTO = ProductCreateDTO.builder()
                .name("Laptop")
                .description("High-end laptop")
                .price(999.99)
                .categoryId(1L)
                .quantity(10)
                .stockMin(5)
                .stockMax(50)
                .build();

        // Configuration de StockEntity
        stockEntity = StockEntity.builder()
                .id(1L)
                .productId(1L)
                .quantity(10)
                .stockMin(5)
                .stockMax(50)
                .lastUpdated(LocalDateTime.now())
                .build();

        // Configuration de OrderItemEntity
        orderItemEntity = new OrderItemEntity();
        orderItemEntity.setId(new OrderItemId(1L, 1L)); // orderId=1, productId=1
        orderItemEntity.setQuantity(2);
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productMapper.toDTOList(List.of(productEntity))).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> result = productService.getAllProducts();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Laptop");
        verify(productRepository).findAll();
        verify(productMapper).toDTOList(List.of(productEntity));
    }

    @Test
    void getProductById_WhenProductExists_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDTO(productEntity)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.getProductById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
        verify(productRepository).findById(1L);
        verify(productMapper).toDTO(productEntity);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ThrowsException() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("ProductEntity", "1");
        verify(productRepository).findById(1L);
        verifyNoInteractions(productMapper);
    }

    @Test
    void getProductsByIds_WhenAllIdsExist_ReturnsProducts() {
        // Arrange
        when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(productEntity));
        when(productMapper.toDTO(productEntity)).thenReturn(productDTO);

        // Act
        List<ProductDTO> result = productService.getProductsByIds(List.of(1L));

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(productRepository).findAllById(List.of(1L));
        verify(productMapper).toDTO(productEntity);
    }

    @Test
    void getProductsByIds_WhenSomeIdsMissing_ThrowsException() {
        // Arrange
        when(productRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(productEntity));

        // Act & Assert
        assertThatThrownBy(() -> productService.getProductsByIds(List.of(1L, 2L)))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("Products not found for IDs: [2]");
        verify(productRepository).findAllById(List.of(1L, 2L));
        verifyNoInteractions(productMapper);
    }

    @Test
    void getAllProductsByCategoryName_ReturnsProducts() {
        // Arrange
        when(productRepository.findAllByCategoryName("Electronics")).thenReturn(List.of(productEntity));
        when(productMapper.toDTOList(List.of(productEntity))).thenReturn(List.of(productDTO));

        // Act
        List<ProductDTO> result = productService.getAllProductsByCategoryName("Electronics");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(productRepository).findAllByCategoryName("Electronics");
        verify(productMapper).toDTOList(List.of(productEntity));
    }

    @Test
    void getAllProductsByOrderId_ReturnsProducts() {
        // Arrange
        when(orderItemRepository.findAllByIdOrderId(1L)).thenReturn(List.of(orderItemEntity));
        when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(productEntity));
        when(productMapper.toDTO(productEntity)).thenReturn(productDTO);

        // Act
        List<ProductDTO> result = productService.getAllProductsByOrderId(1L);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(orderItemRepository).findAllByIdOrderId(1L);
        verify(productRepository).findAllById(List.of(1L));
        verify(productMapper).toDTO(productEntity);
    }

    @Test
    void deleteProductById_WhenProductExists_DeletesProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        productService.deleteProductById(1L);

        // Assert
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProductById_WhenProductDoesNotExist_ThrowsException() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> productService.deleteProductById(1L))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("ProductEntity", "1");
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }

    @Test
    void createProductImage_WhenValidImage_SavesImageAndUpdatesProduct() throws IOException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getOriginalFilename()).thenReturn("laptop.jpg");
        when(file.getInputStream()).thenReturn(mock(java.io.InputStream.class));
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Simuler le dossier d'upload
        Path uploadPath = tempDir.resolve("uploads/images/products");
        Files.createDirectories(uploadPath);

        // Act
        String result = productService.createProductImage(1L, file);

        // Assert
        assertThat(result).startsWith("/images/products/product_1_");
        assertThat(result).endsWith("laptop.jpg");
        assertThat(productEntity.getImageUrl()).isEqualTo(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(productEntity);
    }

    @Test
    void createProductImage_WhenInvalidContentType_ThrowsException() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("text/plain");

        // Act & Assert
        assertThatThrownBy(() -> productService.createProductImage(1L, file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only image files are allowed.");
        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductImage_WhenProductNotFound_ThrowsException() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getOriginalFilename()).thenReturn("laptop.jpg");
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.createProductImage(1L, file))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to store image file: Ressource(s) (ProductEntity) Not Found at ID : 1");
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProduct_WhenValidInput_CreatesProductAndStock() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(stockRepository.save(any(StockEntity.class))).thenReturn(stockEntity);
        when(productMapper.toDTO(productEntity)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.createProduct(productCreateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(ProductEntity.class));
        verify(stockRepository).save(argThat(stock ->
                stock.getProductId().equals(1L) &&
                stock.getQuantity().equals(10) &&
                stock.getStockMin().equals(5) &&
                stock.getStockMax().equals(50)));
        verify(productMapper).toDTO(productEntity);
    }

    @Test
    void createProduct_WhenCategoryNotFound_ThrowsException() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.createProduct(productCreateDTO))
                .isInstanceOf(RessourceNotFoundException.class)
                .hasMessageContaining("CategoryEntity", "1");
        verify(categoryRepository).findById(1L);
        verifyNoInteractions(productRepository, stockRepository, productMapper);
    }

    @Test
    void createProduct_WhenQuantityBelowStockMin_ThrowsException() {
        // Arrange
        productCreateDTO.setQuantity(2); // < stockMin (5)
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));

        // Act & Assert
        assertThatThrownBy(() -> productService.createProduct(productCreateDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be between stockMin and stockMax.");
        verify(categoryRepository).findById(1L);
        verifyNoInteractions(productRepository, stockRepository, productMapper);
    }

    @Test
    void createProduct_WhenQuantityAboveStockMax_ThrowsException() {
        // Arrange
        productCreateDTO.setQuantity(60); // > stockMax (50)
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));

        // Act & Assert
        assertThatThrownBy(() -> productService.createProduct(productCreateDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be between stockMin and stockMax.");
        verify(categoryRepository).findById(1L);
        verifyNoInteractions(productRepository, stockRepository, productMapper);
    }
}