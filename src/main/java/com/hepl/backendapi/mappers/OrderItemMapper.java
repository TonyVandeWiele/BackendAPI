package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.OrderItemDTO;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "productId", target = "id.productId")
    OrderItemEntity toEntity(OrderItemDTO orderLinesDTO);

    @Mapping(source = "id.productId", target = "productId")
    OrderItemDTO toDTO(OrderItemEntity orderItemEntity);

    @Mapping(source = "productId", target = "id.productId")
    List<OrderItemEntity> toEntityList(List<OrderItemDTO> orderLinesDTOList);

    @Mapping(source = "id.productId", target = "productId")
    List<OrderItemDTO> toDTOList(List<OrderItemEntity> orderItemEntityList);
}
