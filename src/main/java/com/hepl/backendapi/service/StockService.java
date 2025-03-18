package com.hepl.backendapi.service;

import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.dto.StockDTO;
import com.hepl.backendapi.entity.StockEntity;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Autowired
    public StockService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    public List<StockDTO> getAllStocks() {
        List<StockEntity> stockEntityList = stockRepository.findAll();

        return stockMapper.toStockDTOList(stockEntityList);
    }

    public StockDTO getStockById(Long id) {
        StockEntity stock = stockRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(StockEntity.class.getSimpleName(), id));
        return stockMapper.toStockDTO(stock);
    }

}
