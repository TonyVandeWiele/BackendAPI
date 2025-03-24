package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.OrderDTO;
import com.hepl.backendapi.entity.dbtransac.OrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderEntity toEntity(OrderEntity orderDTO);
    OrderDTO toDTO(OrderEntity orderEntity);

    List<OrderDTO> toDTOList(List<OrderEntity> orderEntities);
    List<OrderEntity> toEntityList(List<OrderDTO> orderDTOs);
}
