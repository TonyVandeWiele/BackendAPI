package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.entity.dbtransac.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity user);
}
