package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.AddressDTO;
import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDTO(AddressEntity entity);
    AddressEntity toEntity(AddressDTO dto);

    AddressEntity toEntity(AddressCreateDTO dto);
    List<AddressDTO> toDTOList(List<AddressEntity> entities);
}
