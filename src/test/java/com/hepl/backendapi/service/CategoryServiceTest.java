
package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.CategoryDTO;
import com.hepl.backendapi.entity.dbservices.CategoryEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.CategoryMapper;
import com.hepl.backendapi.repository.dbservices.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryEntity categoryEntity;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Electronics");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electronics");
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));
        when(categoryMapper.toCategoryDTOList(anyList())).thenReturn(List.of(categoryDTO));

        // Act
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toCategoryDTOList(anyList());
    }

    @Test
    void testGetCategoryById_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategoryDTO(any(CategoryEntity.class))).thenReturn(categoryDTO);

        // Act
        CategoryDTO result = categoryService.getCategoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toCategoryDTO(any(CategoryEntity.class));
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RessourceNotFoundException.class, () -> categoryService.getCategoryById(2L));

        verify(categoryRepository, times(1)).findById(2L);
        verify(categoryMapper, never()).toCategoryDTO(any());
    }
}
