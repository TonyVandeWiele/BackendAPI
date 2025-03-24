package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.OrderLinesDTO;
import com.hepl.backendapi.entity.dbtransac.OrderLinesEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderLinesMapper {

    OrderLinesEntity toEntity(OrderLinesDTO orderLinesDTO);

    OrderLinesDTO toDTO(OrderLinesEntity orderLinesEntity);

    List<OrderLinesEntity> toEntityList(List<OrderLinesDTO> orderLinesDTOList);

    List<OrderLinesDTO> toDTOList(List<OrderLinesEntity> orderLinesEntityList);
}
