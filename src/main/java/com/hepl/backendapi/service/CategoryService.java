package com.hepl.backendapi.service;

import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.dto.CategoryDTO;
import com.hepl.backendapi.entity.CategoryEntity;
import com.hepl.backendapi.mappers.CategoryMapper;
import com.hepl.backendapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    private CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        return categoryMapper.toCategoryDTOList(categoryEntityList);
    }
    public CategoryDTO getCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(CategoryEntity.class.getSimpleName(), id));
        return categoryMapper.toCategoryDTO(categoryEntity);
    }
}
