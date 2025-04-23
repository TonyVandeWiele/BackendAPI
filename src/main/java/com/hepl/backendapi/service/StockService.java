package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import com.hepl.backendapi.utils.UtilsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public StockDTO getStockByProductId(Long productId) {
        StockEntity stock = stockRepository.findByProductId(productId).orElseThrow(() -> new RessourceNotFoundException(StockEntity.class.getSimpleName(), productId));
        return stockMapper.toStockDTO(stock);
    }

    @Transactional
    public StockDTO updateStock(Long productId, Integer quantity)
    {
        StockEntity stockEntity = stockRepository.findByProductId(productId).orElseThrow(() -> new RessourceNotFoundException("This product does have a stock", productId));

        UtilsClass.validateQuantityInRange(quantity, stockEntity);

        stockEntity.setQuantity(quantity);
        stockRepository.save(stockEntity);

        return stockMapper.toStockDTO(stockEntity);
    }
}
