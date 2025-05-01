package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.SAVDTO;
import com.hepl.backendapi.dto.post.SAVCreateDTO;
import com.hepl.backendapi.entity.dbservices.SAVEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.SAVMapper;
import com.hepl.backendapi.repository.dbservices.SAVRepository;
import com.hepl.backendapi.repository.dbtransac.OrderRepository;
import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SAVServiceTest {

    @Mock
    private SAVRepository savRepository;

    @Mock
    private SAVMapper savMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SAVService savService;

    private SAVEntity savEntity;
    private SAVDTO savDTO;
    private SAVCreateDTO savCreateDTO;

    @BeforeEach
    void setUp() {
        // Données de test
        savEntity = SAVEntity.builder()
                .id(1L)
                .orderId(100L)
                .customerId(1L)
                .description("Problème avec le produit")
                .requestType(RequestTypeEnum.Open)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savDTO = SAVDTO.builder()
                .id(1L)
                .orderId(100L)
                .customerId(1L)
                .description("Problème avec le produit")
                .requestType(RequestTypeEnum.Open)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        savCreateDTO = SAVCreateDTO.builder()
                .orderId(100L)
                .description("Problème avec le produit")
                .build();
    }

    // Tests pour createSAVRequest
    @Test
    void createSAVRequest_Success_ReturnsSAVDTO() {
        when(orderRepository.existsById(100L)).thenReturn(true);
        when(savRepository.save(any(SAVEntity.class))).thenReturn(savEntity);
        when(savMapper.toDTO(savEntity)).thenReturn(savDTO);

        SAVDTO result = savService.createSAVRequest(savCreateDTO);

        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        assertEquals(RequestTypeEnum.Open, result.getRequestType());
        assertEquals("Problème avec le produit", result.getDescription());
        assertEquals(1L, result.getCustomerId());
        verify(orderRepository).existsById(100L);
        verify(savRepository).save(any(SAVEntity.class));
        verify(savMapper).toDTO(savEntity);
    }

    @Test
    void createSAVRequest_OrderNotFound_ThrowsRessourceNotFoundException() {
        when(orderRepository.existsById(100L)).thenReturn(false);

        assertThrows(RessourceNotFoundException.class, () -> savService.createSAVRequest(savCreateDTO));
        verify(orderRepository).existsById(100L);
        verify(savRepository, never()).save(any());
        verify(savMapper, never()).toDTO(any());
    }

    // Tests pour getAllSAVRequests
    @Test
    void getAllSAVRequests_Success_ReturnsSAVDTOList() {
        when(savRepository.findAll()).thenReturn(List.of(savEntity));
        when(savMapper.toDTO(savEntity)).thenReturn(savDTO);

        List<SAVDTO> result = savService.getAllSAVRequests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getOrderId());
        assertEquals(RequestTypeEnum.Open, result.get(0).getRequestType());
        verify(savRepository).findAll();
        verify(savMapper).toDTO(savEntity);
    }

    @Test
    void getAllSAVRequests_EmptyList_ReturnsEmptyList() {
        when(savRepository.findAll()).thenReturn(List.of());

        List<SAVDTO> result = savService.getAllSAVRequests();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(savRepository).findAll();
        verify(savMapper, never()).toDTO(any());
    }

    // Tests pour updateSAVStatus
    @Test
    void updateSAVStatus_Success_ReturnsUpdatedSAVDTO() {
        when(savRepository.findById(1L)).thenReturn(Optional.of(savEntity));
        when(savRepository.save(savEntity)).thenReturn(savEntity);
        when(savMapper.toDTO(savEntity)).thenAnswer(invocation -> {
            SAVEntity entity = invocation.getArgument(0);
            return SAVDTO.builder()
                    .id(entity.getId())
                    .orderId(entity.getOrderId())
                    .customerId(entity.getCustomerId())
                    .description(entity.getDescription())
                    .requestType(entity.getRequestType())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        });

        SAVDTO result = savService.updateSAVStatus(1L, RequestTypeEnum.InProgress);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(RequestTypeEnum.InProgress, result.getRequestType());
        verify(savRepository).findById(1L);
        verify(savRepository).save(savEntity);
        verify(savMapper).toDTO(savEntity);
    }

    @Test
    void updateSAVStatus_SAVNotFound_ThrowsRessourceNotFoundException() {
        when(savRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> savService.updateSAVStatus(1L, RequestTypeEnum.InProgress));
        verify(savRepository).findById(1L);
        verify(savRepository, never()).save(any());
        verify(savMapper, never()).toDTO(any());
    }

    @Test
    void updateSAVStatus_DifferentStatus_ReturnsUpdatedSAVDTO() {
        when(savRepository.findById(1L)).thenReturn(Optional.of(savEntity));
        when(savRepository.save(savEntity)).thenReturn(savEntity);
        when(savMapper.toDTO(savEntity)).thenAnswer(invocation -> {
            SAVEntity entity = invocation.getArgument(0);
            return SAVDTO.builder()
                    .id(entity.getId())
                    .orderId(entity.getOrderId())
                    .customerId(entity.getCustomerId())
                    .description(entity.getDescription())
                    .requestType(entity.getRequestType())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        });

        SAVDTO result = savService.updateSAVStatus(1L, RequestTypeEnum.Closed);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(RequestTypeEnum.Closed, result.getRequestType());
        verify(savRepository).findById(1L);
        verify(savRepository).save(savEntity);
        verify(savMapper).toDTO(savEntity);
    }
}