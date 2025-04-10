package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.UserCreateDTO;
import com.hepl.backendapi.dto.update.UserUpdateDTO;
import com.hepl.backendapi.entity.dbtransac.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {

    UserEntity toUserEntity(UserDTO userDTO);
    UserDTO toUserDTO(UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "clientAccountNumber", ignore = true)
    void updateUserFromDto(UserUpdateDTO dto, @MappingTarget UserEntity entity);
}
