package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.UserDTO;
import com.hepl.backendapi.entity.dbservices.UserEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.UserMapper;
import com.hepl.backendapi.repository.dbservices.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userDTO = new UserDTO();
    }

    @Test
    void testGetUserById_Success() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toUserDTO(userEntity);
    }

    @Test
    void testGetUserById_UserNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> userService.getUserById(userId));
    }
}
