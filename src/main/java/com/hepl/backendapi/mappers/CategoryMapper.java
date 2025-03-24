package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.CategoryDTO;
import com.hepl.backendapi.entity.dbservices.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(CategoryEntity category);
    CategoryEntity toCategoryEntity(CategoryDTO categoryDTO);
    List<CategoryDTO> toCategoryDTOList(List<CategoryEntity> categoryEntities);
    List<CategoryEntity> toCategoryEntityList(List<CategoryDTO> categoryDTOList);
}
