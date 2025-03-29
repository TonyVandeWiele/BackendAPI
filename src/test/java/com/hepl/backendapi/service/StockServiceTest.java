package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.StockDTO;
import com.hepl.backendapi.entity.dbservices.StockEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.dbservices.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    private StockEntity stock1;
    private StockEntity stock2;
    private StockDTO stockDTO1;
    private StockDTO stockDTO2;

    @BeforeEach
    void setUp() {
        stock1 = new StockEntity(); 
        stock2 = new StockEntity();

        stockDTO1 = new StockDTO();
        stockDTO2 = new StockDTO();
    }

    @Test
    void testGetAllStocks_Success() {
        List<StockEntity> stockEntities = Arrays.asList(stock1, stock2);
        List<StockDTO> stockDTOs = Arrays.asList(stockDTO1, stockDTO2);

        when(stockRepository.findAll()).thenReturn(stockEntities);
        when(stockMapper.toStockDTOList(stockEntities)).thenReturn(stockDTOs);

        List<StockDTO> result = stockService.getAllStocks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(stockRepository, times(1)).findAll();
        verify(stockMapper, times(1)).toStockDTOList(stockEntities);
    }

    @Test
    void testGetStockById_Success() {
        Long stockId = 1L;
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(stock1));
        when(stockMapper.toStockDTO(stock1)).thenReturn(stockDTO1);

        StockDTO result = stockService.getStockById(stockId);

        assertNotNull(result);
        verify(stockRepository, times(1)).findById(stockId);
        verify(stockMapper, times(1)).toStockDTO(stock1);
    }

}
