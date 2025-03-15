package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.ProductDTO;
import com.hepl.backendapi.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "stock.id", target = "stockId")
    ProductDTO toDTO(ProductEntity entity);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "stockId", target = "stock.id")
    ProductEntity toEntity(ProductDTO dto);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "stock.id", target = "stockId")
    List<ProductDTO> toDTOList(List<ProductEntity> entities);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "stockId", target = "stock.id")
    List<ProductEntity> toEntityList(List<ProductDTO> dtos);
}
