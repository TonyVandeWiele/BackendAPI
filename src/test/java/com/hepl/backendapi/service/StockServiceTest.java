package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.StockDTO;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.exception.InvalidValueException;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.StockMapper;
import com.hepl.backendapi.repository.dbtransac.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    private StockEntity stockEntity;
    private StockDTO stockDTO;

    @BeforeEach
    void setUp() {
        // Données de test
        stockEntity = StockEntity.builder()
                .id(1L)
                .productId(100L)
                .quantity(50)
                .stockMin(10)
                .stockMax(100)
                .lastUpdated(LocalDateTime.now())
                .build();

        stockDTO = StockDTO.builder()
                .id(1L)
                .productId(100L)
                .quantity(50)
                .stockMin(10)
                .stockMax(100)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    // Tests pour getAllStocks
    @Test
    void getAllStocks_Success_ReturnsStockDTOList() {
        when(stockRepository.findAll()).thenReturn(List.of(stockEntity));
        when(stockMapper.toStockDTOList(List.of(stockEntity))).thenReturn(List.of(stockDTO));

        List<StockDTO> result = stockService.getAllStocks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getProductId());
        assertEquals(50, result.get(0).getQuantity());
        verify(stockRepository).findAll();
        verify(stockMapper).toStockDTOList(List.of(stockEntity));
    }

    @Test
    void getAllStocks_EmptyList_ReturnsEmptyList() {
        when(stockRepository.findAll()).thenReturn(List.of());
        when(stockMapper.toStockDTOList(List.of())).thenReturn(List.of());

        List<StockDTO> result = stockService.getAllStocks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockRepository).findAll();
        verify(stockMapper).toStockDTOList(List.of());
    }

    // Tests pour getStockByProductId
    @Test
    void getStockByProductId_Success_ReturnsStockDTO() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.of(stockEntity));
        when(stockMapper.toStockDTO(stockEntity)).thenReturn(stockDTO);

        StockDTO result = stockService.getStockByProductId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getProductId());
        assertEquals(50, result.getQuantity());
        verify(stockRepository).findByProductId(100L);
        verify(stockMapper).toStockDTO(stockEntity);
    }

    @Test
    void getStockByProductId_NotFound_ThrowsRessourceNotFoundException() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> stockService.getStockByProductId(100L));
        verify(stockRepository).findByProductId(100L);
        verify(stockMapper, never()).toStockDTO(any());
    }

    // Tests pour updateStock
    @Test
    void updateStock_Success_ReturnsUpdatedStockDTO() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(stockEntity)).thenReturn(stockEntity);
    
        // Créer un StockDTO avec la quantité mise à jour
        StockDTO updatedStockDTO = StockDTO.builder()
                .id(1L)
                .productId(100L)
                .quantity(75) // Nouvelle quantité
                .stockMin(10)
                .stockMax(100)
                .lastUpdated(stockDTO.getLastUpdated())
                .build();
        when(stockMapper.toStockDTO(stockEntity)).thenReturn(updatedStockDTO);
    
        StockDTO result = stockService.updateStock(100L, 75);
    
        assertNotNull(result);
        assertEquals(100L, result.getProductId());
        assertEquals(75, result.getQuantity());
        verify(stockRepository).findByProductId(100L);
        verify(stockRepository).save(stockEntity);
        verify(stockMapper).toStockDTO(stockEntity);
    }

    @Test
    void updateStock_ProductNotFound_ThrowsRessourceNotFoundException() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> stockService.updateStock(100L, 75));
        verify(stockRepository).findByProductId(100L);
        verify(stockRepository, never()).save(any());
        verify(stockMapper, never()).toStockDTO(any());
    }

    @Test
    void updateStock_QuantityBelowMin_ThrowsInvalidValueException() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.of(stockEntity));

        assertThrows(InvalidValueException.class, () -> stockService.updateStock(100L, 5));
        verify(stockRepository).findByProductId(100L);
        verify(stockRepository, never()).save(any());
        verify(stockMapper, never()).toStockDTO(any());
    }

    @Test
    void updateStock_QuantityAboveMax_ThrowsInvalidValueException() {
        when(stockRepository.findByProductId(100L)).thenReturn(Optional.of(stockEntity));

        assertThrows(InvalidValueException.class, () -> stockService.updateStock(100L, 150));
        verify(stockRepository).findByProductId(100L);
        verify(stockRepository, never()).save(any());
        verify(stockMapper, never()).toStockDTO(any());
    }
}