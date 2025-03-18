package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.UserDTO;
import com.hepl.backendapi.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity user);
}
