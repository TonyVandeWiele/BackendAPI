package com.hepl.backendapi.service;


import com.hepl.backendapi.dto.generic.SAVDTO;
import com.hepl.backendapi.dto.post.SAVCreateDTO;
import com.hepl.backendapi.entity.dbservices.SAVEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.SAVMapper;
import com.hepl.backendapi.repository.dbservices.SAVRepository;
import com.hepl.backendapi.repository.dbtransac.OrderRepository;
import com.hepl.backendapi.utils.enumeration.RequestTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SAVService {

    private final SAVRepository savRepository;

    private final SAVMapper savMapper;

    private final OrderRepository orderRepository;

    public SAVService(SAVRepository savRepository, @Qualifier("SAVMapperImpl")SAVMapper savMapper, OrderRepository orderRepository) {
        this.savRepository = savRepository;
        this.savMapper = savMapper;
        this.orderRepository = orderRepository;
    }

    // Créer un ticket SAV
    @Transactional
    public SAVDTO createSAVRequest(SAVCreateDTO savCreateDTO) {

        // Vérifier si l'OrderID existe dans la base de données
        if (!orderRepository.existsById(savCreateDTO.getOrderId())) {
            throw new RessourceNotFoundException("Order ID not found: " + savCreateDTO.getOrderId());
        }

        SAVEntity savEntity = SAVEntity.builder()
                .requestType(RequestTypeEnum.Open)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .orderId(savCreateDTO.getOrderId())
                .customerId(1L)
                .build();
        savEntity = savRepository.save(savEntity);
        return savMapper.toDTO(savEntity);
    }

    // Récupérer tous les tickets SAV
    @Transactional
    public List<SAVDTO> getAllSAVRequests() {
        List<SAVEntity> savEntities = savRepository.findAll();
        return savEntities.stream()
                .map(savMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Modifier le statut d'un ticket SAV
    @Transactional
    public SAVDTO updateSAVStatus(Long id, RequestTypeEnum newStatus) {
        SAVEntity savEntity = savRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("SAV ticket not found with ID: " + id));

        savEntity.setRequestType(newStatus);  // Modifier le statut du ticket SAV
        savEntity = savRepository.save(savEntity);
        return savMapper.toDTO(savEntity);
    }
}

