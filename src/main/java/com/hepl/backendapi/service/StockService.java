package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    public StockDTO getStockByProductId(Long productId) {
        StockEntity stock = stockRepository.findByProductId(productId).orElseThrow(() -> new RessourceNotFoundException(StockEntity.class.getSimpleName(), productId));
        return stockMapper.toStockDTO(stock);
    }

    public StockDTO updateStock(Long productId, Integer quantity)
    {
        StockEntity stockEntity = stockRepository.findByProductId(productId).orElseThrow(() -> new RessourceNotFoundException("This product does have a stock", productId));

        stockEntity.setQuantity(quantity);
        stockRepository.save(stockEntity);

        return stockMapper.toStockDTO(stockEntity);
    }
}
