package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockEntity toStockEntity(StockDTO stockDTO);
    StockDTO toStockDTO(StockEntity stockEntity);
    List<StockDTO> toStockDTOList(List<StockEntity> stockEntities);
    List<StockEntity> toStockEntityList(List<StockDTO> stockDTOList);
}
