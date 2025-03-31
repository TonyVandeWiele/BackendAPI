package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.OrderItemDTO;
import com.hepl.backendapi.entity.dbtransac.OrderItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemEntity toEntity(OrderItemDTO orderLinesDTO);

    OrderItemDTO toDTO(OrderItemEntity orderItemEntity);

    List<OrderItemEntity> toEntityList(List<OrderItemDTO> orderLinesDTOList);

    List<OrderItemDTO> toDTOList(List<OrderItemEntity> orderItemEntityList);
}
