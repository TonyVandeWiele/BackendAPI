package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.dto.post.UserCreateDTO;
import com.hepl.backendapi.dto.update.UserUpdateDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.entity.dbtransac.UserEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.AddressMapper;
import com.hepl.backendapi.mappers.UserMapper;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import com.hepl.backendapi.repository.dbtransac.UserRepository;
import com.hepl.backendapi.utils.enumeration.MaritalStatus;
import com.hepl.backendapi.utils.enumeration.RoleEnum;
import com.hepl.backendapi.utils.enumeration.SexeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserDTO userDTO;
    private AddressEntity addressEntity;
    private AddressCreateDTO addressCreateDTO;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        // Sample data for tests
        addressEntity = AddressEntity.builder()
                .id(1L)
                .number("123")
                .street("Main St")
                .city("City")
                .zipCode("12345")
                .country("Country")
                .build();

        addressCreateDTO = AddressCreateDTO.builder()
                .number("123")
                .street("Main St")
                .city("City")
                .zipCode("12345")
                .country("Country")
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .address(addressEntity)
                .registrationDate(LocalDateTime.now())
                .clientAccountNumber("ACC123")
                .birthday(LocalDate.of(1990, 1, 1))
                .maritalStatus(MaritalStatus.single)
                .sexe(SexeEnum.M)
                .mensuelSalary(5000.0)
                .role(RoleEnum.CLIENT)
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .registrationDate(LocalDateTime.now())
                .clientAccountNumber("ACC123")
                .birthday(LocalDate.of(1990, 1, 1))
                .maritalStatus(MaritalStatus.single)
                .sexe(SexeEnum.M)
                .mensuelSalary(5000.0)
                .role(RoleEnum.CLIENT)
                .build();

        userCreateDTO = UserCreateDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .address(addressCreateDTO)
                .clientAccountNumber("ACC123")
                .birthday(LocalDate.of(1990, 1, 1))
                .maritalStatus(MaritalStatus.single)
                .sexe(SexeEnum.M)
                .mensuelSalary(5000.0)
                .role(RoleEnum.CLIENT)
                .build();

        userUpdateDTO = UserUpdateDTO.builder()
                .name("Jane Doe")
                .phone("+0987654321")
                .address(addressCreateDTO)
                .birthday(LocalDate.of(1992, 2, 2))
                .maritalStatus(MaritalStatus.married)
                .sexe(SexeEnum.F)
                .mensuelSalary(6000.0)
                .build();
    }

    // Tests for getUserById
    @Test
    void getUserById_Success_ReturnsUserDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userMapper).toUserDTO(userEntity);
    }

    @Test
    void getUserById_NotFound_ThrowsRessourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
        verify(userMapper, never()).toUserDTO(any());
    }

    // Tests for getAllUsers
    @Test
    void getAllUsers_Success_ReturnsUserDTOList() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toUserDTOList(List.of(userEntity))).thenReturn(List.of(userDTO));

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        verify(userRepository).findAll();
        verify(userMapper).toUserDTOList(List.of(userEntity));
    }

    @Test
    void getAllUsers_EmptyList_ReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userMapper.toUserDTOList(List.of())).thenReturn(List.of());

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
        verify(userMapper).toUserDTOList(List.of());
    }

    // Tests for updateUserDTO
    @Test
    void updateUserDTO_SuccessWithNewAddress_ReturnsUpdatedUserDTO() {
        // Create a different address for the update
        AddressCreateDTO newAddressCreateDTO = AddressCreateDTO.builder()
                .number("456")
                .street("New St")
                .city("New City")
                .zipCode("67890")
                .country("New Country")
                .build();
        userUpdateDTO.setAddress(newAddressCreateDTO);
    
        AddressEntity newAddressEntity = AddressEntity.builder()
                .id(2L)
                .number("456")
                .street("New St")
                .city("New City")
                .zipCode("67890")
                .country("New Country")
                .build();
    
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(addressMapper.toEntity(newAddressCreateDTO)).thenReturn(newAddressEntity);
        when(addressRepository.save(newAddressEntity)).thenReturn(newAddressEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);
    
        UserDTO result = userService.updateUserDTO(1L, userUpdateDTO);
    
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userMapper).updateUserFromDto(userUpdateDTO, userEntity);
        verify(addressMapper).toEntity(newAddressCreateDTO);
        verify(addressRepository).save(newAddressEntity);
        verify(userRepository).save(userEntity);
        verify(userMapper).toUserDTO(userEntity);
    }

    @Test
    void updateUserDTO_SameAddress_DoesNotSaveNewAddress() {
        userEntity.setAddress(addressEntity);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(addressMapper.toEntity(addressCreateDTO)).thenReturn(addressEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.updateUserDTO(1L, userUpdateDTO);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userMapper).updateUserFromDto(userUpdateDTO, userEntity);
        verify(addressMapper).toEntity(addressCreateDTO);
        verify(addressRepository, never()).save(any());
        verify(userRepository).save(userEntity);
        verify(userMapper).toUserDTO(userEntity);
    }

    @Test
    void updateUserDTO_NotFound_ThrowsRessourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> userService.updateUserDTO(1L, userUpdateDTO));
        verify(userRepository).findById(1L);
        verify(userMapper, never()).updateUserFromDto(any(), any());
        verify(addressRepository, never()).save(any());
    }

    // Tests for createUser
    @Test
    void createUser_SuccessWithAddress_ReturnsUserDTO() {
        when(addressMapper.toEntity(addressCreateDTO)).thenReturn(addressEntity);
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country")).thenReturn(Optional.empty());
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(addressMapper).toEntity(addressCreateDTO);
        verify(addressRepository).findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country");
        verify(addressRepository).save(addressEntity);
        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).toUserDTO(userEntity);
    }

    @Test
    void createUser_WithoutName_UsesEmailPrefix() {
        userCreateDTO.setName(null);
        when(addressMapper.toEntity(addressCreateDTO)).thenReturn(addressEntity);
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country")).thenReturn(Optional.empty());
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
        verify(userRepository).save(argThat(user -> user.getName().equals("john.doe")));
    }

   @Test
    void createUser_ExistingAddress_UsesExistingAddress() {
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country")).thenReturn(Optional.of(addressEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
        verify(addressRepository).findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country");
        // verify(addressMapper, never()).toEntity(any()); // Explicitly verify toEntity is not called
        verify(addressRepository, never()).save(any());
        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).toUserDTO(userEntity);
    }

    // Tests for saveAddressIfNotExists
    @Test
    void saveAddressIfNotExists_NewAddress_SavesAndReturnsAddressEntity() {
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country")).thenReturn(Optional.empty());
        when(addressMapper.toEntity(addressCreateDTO)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);

        AddressEntity result = userService.saveAddressIfNotExists(addressCreateDTO);

        assertNotNull(result);
        assertEquals("Main St", result.getStreet());
        verify(addressRepository).findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country");
        verify(addressMapper).toEntity(addressCreateDTO);
        verify(addressRepository).save(addressEntity);
    }

    @Test
    void saveAddressIfNotExists_ExistingAddress_ReturnsExistingAddress() {
        when(addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country")).thenReturn(Optional.of(addressEntity));

        AddressEntity result = userService.saveAddressIfNotExists(addressCreateDTO);

        assertNotNull(result);
        assertEquals("Main St", result.getStreet());
        verify(addressRepository).findByNumberAndStreetAndCityAndZipCodeAndCountry(
                "123", "Main St", "City", "12345", "Country");
        // verify(addressMapper, never()).toEntity(any());
        verify(addressRepository, never()).save(any());
    }
}