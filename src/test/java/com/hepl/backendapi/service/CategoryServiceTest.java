package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.CategoryDTO;
import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.CategoryMapper;
import com.hepl.backendapi.repository.dbtransac.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    public CategoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories_Success() {
        List<CategoryEntity> categoryEntities = Arrays.asList(
            new CategoryEntity(1L, "Category 1", "Description 1"),
            new CategoryEntity(2L, "Category 2", "Description 2")
        );

        List<CategoryDTO> categoryDTOs = Arrays.asList(
            CategoryDTO.builder().id(1L).name("Category 1").description("Description 1").build(),
            CategoryDTO.builder().id(2L).name("Category 2").description("Description 2").build()
        );

        when(categoryRepository.findAll()).thenReturn(categoryEntities);
        when(categoryMapper.toCategoryDTOList(categoryEntities)).thenReturn(categoryDTOs);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(categoryDTOs.size(), result.size());
        verify(categoryRepository).findAll();
        verify(categoryMapper).toCategoryDTOList(categoryEntities);
    }

    @Test
    public void testGetCategoryById_Success() {
        Long categoryId = 1L;
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "Category 1", "Description 1");
        CategoryDTO categoryDTO = CategoryDTO.builder()
            .id(categoryId)
            .name("Category 1")
            .description("Description 1")
            .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toCategoryDTO(categoryEntity)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryDTO, result);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toCategoryDTO(categoryEntity);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        RessourceNotFoundException exception = assertThrows(RessourceNotFoundException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        // Correction ici : on fait matcher le vrai message généré par ton service
        assertEquals("Ressource(s) (CategoryEntity) Not Found at ID : " + categoryId, exception.getMessage());

        verify(categoryRepository).findById(categoryId);
    }
}
