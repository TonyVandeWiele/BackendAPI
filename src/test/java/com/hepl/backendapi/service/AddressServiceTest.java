package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.AddressMapper;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    public AddressServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAddressById_Success() {
        Long addressId = 1L;
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(addressId);
        addressEntity.setStreet("Main St");
        addressEntity.setCity("Springfield");
        addressEntity.setNumber("123");
        addressEntity.setZipCode("12345");
        addressEntity.setCountry("USA");

        AddressDTO addressDTO = AddressDTO.builder()
                .id(addressId)
                .street("Main St")
                .city("Springfield")
                .number("123")
                .zipCode("12345")
                .country("USA")
                .build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));
        when(addressMapper.toDTO(addressEntity)).thenReturn(addressDTO);

        AddressDTO result = addressService.getAddressById(addressId);

        assertNotNull(result);
        assertEquals(addressDTO, result);
        verify(addressRepository).findById(addressId);
        verify(addressMapper).toDTO(addressEntity);
    }

    @Test
    public void testGetAddressById_NotFound() {
        Long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        RessourceNotFoundException exception = assertThrows(RessourceNotFoundException.class, () -> {
            addressService.getAddressById(addressId);
        });

        assertEquals("Ressource(s) (AddressEntity) Not Found at ID : " + addressId, exception.getMessage());
        verify(addressRepository).findById(addressId);
    }

    @Test
    public void testGetAddressesByClientId_Success() {
        Long clientId = 1L;
        List<Long> addressIds = Arrays.asList(1L, 2L);
        List<AddressEntity> addressEntities = Arrays.asList(new AddressEntity(), new AddressEntity());

        addressEntities.get(0).setId(1L);
        addressEntities.get(0).setStreet("Main St");
        addressEntities.get(0).setCity("Springfield");
        addressEntities.get(0).setNumber("123");
        addressEntities.get(0).setZipCode("12345");
        addressEntities.get(0).setCountry("USA");

        addressEntities.get(1).setId(2L);
        addressEntities.get(1).setStreet("Second St");
        addressEntities.get(1).setCity("Shelbyville");
        addressEntities.get(1).setNumber("456");
        addressEntities.get(1).setZipCode("67890");
        addressEntities.get(1).setCountry("USA");

        List<AddressDTO> addressDTOs = Arrays.asList(
                AddressDTO.builder().id(1L).street("Main St").city("Springfield").number("123").zipCode("12345").country("USA").build(),
                AddressDTO.builder().id(2L).street("Second St").city("Shelbyville").number("456").zipCode("67890").country("USA").build()
        );

        when(addressRepository.findAddressIdsByClientId(clientId)).thenReturn(addressIds);
        when(addressRepository.findAllById(addressIds)).thenReturn(addressEntities);
        when(addressMapper.toDTOList(addressEntities)).thenReturn(addressDTOs);

        List<AddressDTO> result = addressService.getAddressesByClientId(clientId);

        assertNotNull(result);
        assertEquals(addressDTOs.size(), result.size());
        verify(addressRepository).findAddressIdsByClientId(clientId);
        verify(addressRepository).findAllById(addressIds);
        verify(addressMapper).toDTOList(addressEntities);
    }

    @Test
    public void testGetAddressesByClientId_NoAddresses() {
        Long clientId = 1L;
    
        when(addressRepository.findAddressIdsByClientId(clientId)).thenReturn(Collections.emptyList());
        when(addressRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(addressMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList()); // <<<<< ajouter cette ligne
    
        List<AddressDTO> result = addressService.getAddressesByClientId(clientId);
    
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository).findAddressIdsByClientId(clientId);
        verify(addressRepository).findAllById(Collections.emptyList());
        verify(addressMapper).toDTOList(Collections.emptyList()); // <<<<< vérifier que c'est bien appelé
    }
    
}
