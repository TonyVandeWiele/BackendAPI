package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.AddressMapper;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;

    private AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public AddressDTO getAddressById(Long id) {
        AddressEntity addressEntity = addressRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(AddressEntity.class.getSimpleName(), id));;
        return addressMapper.toDTO(addressEntity);
    }

    public List<AddressDTO> getAddressesByClientId(Long clientId) {
        List<Long> addressIds = addressRepository.findAddressIdsByClientId(clientId);
        List<AddressEntity> addressEntityList = new ArrayList<>();
        for (Long addressId : addressIds) {
            AddressEntity addressEntity = addressRepository.findById(addressId).orElse(null);
            if (addressEntity != null) {
                addressEntityList.add(addressEntity);
            }
        }
        return addressMapper.toDTOList(addressEntityList);
    }
}
