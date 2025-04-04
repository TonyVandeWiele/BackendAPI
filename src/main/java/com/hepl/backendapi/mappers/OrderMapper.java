package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.OrderDTO;
import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "trackingId", source = "tracking.id")
    OrderEntity toEntity(OrderDTO orderDTO);

    OrderDTO toDTO(OrderEntity orderEntity);

    List<OrderDTO> toDTOList(List<OrderEntity> orderEntities);
    @Mapping(target = "trackingId", source = "tracking.id")
    List<OrderEntity> toEntityList(List<OrderDTO> orderDTOs);
}
