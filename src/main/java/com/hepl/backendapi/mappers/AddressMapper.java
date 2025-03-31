package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.entity.dbservices.AddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDTO(AddressEntity entity);
    AddressEntity toEntity(AddressDTO dto);
}
