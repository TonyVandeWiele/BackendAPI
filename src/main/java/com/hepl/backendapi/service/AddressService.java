package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.AddressMapper;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import com.hepl.backendapi.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {
    final AddressRepository addressRepository;
    final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    public AddressDTO getAddressById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(AddressEntity.class.getSimpleName(), id));;
        return addressMapper.toDTO(addressEntity);
    }

    @Transactional
    public List<AddressDTO> getAddressesByClientId(Long clientId) {
        List<Long> addressIds = addressRepository.findAddressIdsByClientId(clientId);
        List<AddressEntity> addressEntityList = addressRepository.findAllById(addressIds);
        return addressMapper.toDTOList(addressEntityList);
    }
}
