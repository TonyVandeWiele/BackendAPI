package com.hepl.backendapi.service;

import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.entity.dbtransac.UserEntity;
import com.hepl.backendapi.mappers.UserMapper;
import com.hepl.backendapi.repository.dbtransac.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(UserEntity.class.getSimpleName(), id));
        return userMapper.toUserDTO(userEntity);
    }
}
