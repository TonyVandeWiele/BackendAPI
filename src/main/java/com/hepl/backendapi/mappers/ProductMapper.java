package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.ProductDTO;
import com.hepl.backendapi.dto.post.ProductCreateDTO;
import com.hepl.backendapi.entity.dbtransac.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(ProductEntity entity);

    ProductEntity toEntity(ProductDTO dto);

    ProductEntity toEntity(ProductCreateDTO dto);

    List<ProductDTO> toDTOList(List<ProductEntity> entities);

    List<ProductEntity> toEntityList(List<ProductDTO> dtos);
}
