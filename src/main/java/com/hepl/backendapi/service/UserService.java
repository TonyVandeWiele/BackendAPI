package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.post.AddressCreateDTO;
import com.hepl.backendapi.dto.post.UserCreateDTO;
import com.hepl.backendapi.dto.update.UserUpdateDTO;
import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.entity.dbtransac.UserEntity;
import com.hepl.backendapi.mappers.AddressMapper;
import com.hepl.backendapi.mappers.UserMapper;
import com.hepl.backendapi.repository.dbtransac.AddressRepository;
import com.hepl.backendapi.repository.dbtransac.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;

    final AddressRepository addressRepository;
    final AddressMapper addressMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, AddressRepository addressRepository, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    public UserDTO getUserById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RessourceNotFoundException(UserEntity.class.getSimpleName(), id));
        return userMapper.toUserDTO(userEntity);
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userMapper.toUserDTOList(userEntities);
    }

    @Transactional
    public UserDTO updateUserDTO(Long id, UserUpdateDTO userUpdateDTO) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(UserEntity.class.getSimpleName(), id));

        // Mapping des nouveaux champs
        userMapper.updateUserFromDto(userUpdateDTO, existingUser);

        // Si l'adresse a été modifiée et qu'elle est différente de l'existante
        if (userUpdateDTO.getAddress() != null) {

            AddressEntity newAddress = addressMapper.toEntity(userUpdateDTO.getAddress());

            AddressEntity currentAddress = existingUser.getAddress();

            // Si l'utilisateur n'a pas d'adresse ou si l'adresse est différente
            if (currentAddress == null || !addressesAreEqual(newAddress, currentAddress)) {
                AddressEntity newAddressSaved = addressRepository.save(newAddress);
                existingUser.setAddress(newAddressSaved);
            }
        }

        UserEntity updatedUser = userRepository.save(existingUser);

        return userMapper.toUserDTO(updatedUser);
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {

        String name = userCreateDTO.getName();
        if (name == null || name.isBlank()) {
            name = userCreateDTO.getEmail().split("@")[0];
        }

        UserEntity userEntity = UserEntity.builder()
                .email(userCreateDTO.getEmail())
                .phone(userCreateDTO.getPhone())
                .name(name)
                .birthday(userCreateDTO.getBirthday())
                .registrationDate(LocalDateTime.now())
                .maritalStatus(userCreateDTO.getMaritalStatus())
                .mensuelSalary(userCreateDTO.getMensuelSalary())
                .sexe(userCreateDTO.getSexe())
                .clientAccountNumber(userCreateDTO.getClientAccountNumber())
                .role(userCreateDTO.getRole())
                .build();

        if(userCreateDTO.getAddress() != null) {
            AddressEntity addressEntity = saveAddressIfNotExists(userCreateDTO.getAddress());
            userEntity.setAddress(addressEntity);
        }

        UserEntity userEntitySaved = userRepository.save(userEntity);
        return userMapper.toUserDTO(userEntitySaved);
    }


    @Transactional
    public AddressEntity saveAddressIfNotExists(AddressCreateDTO addressDTO) {
        // Vérifie si l'adresse existe déjà dans la base de données
        Optional<AddressEntity> existingAddress = addressRepository.findByNumberAndStreetAndCityAndZipCodeAndCountry(addressDTO.getNumber(), addressDTO.getStreet(), addressDTO.getCity() ,addressDTO.getZipCode(), addressDTO.getCountry());
        if (existingAddress.isPresent()) {
            return existingAddress.get();  // Retourne l'adresse existante
        }

        // Si l'adresse n'existe pas, on la crée
        AddressEntity newAddress = addressMapper.toEntity(addressDTO);
        return addressRepository.save(newAddress);
    }

    private boolean addressesAreEqual(AddressEntity a1, AddressEntity a2) {
        return Objects.equals(a1.getStreet(), a2.getStreet()) &&
                Objects.equals(a1.getCity(), a2.getCity()) &&
                Objects.equals(a1.getZipCode(), a2.getZipCode()) &&
                Objects.equals(a1.getCountry(), a2.getCountry()) &&
                Objects.equals(a1.getNumber(), a2.getNumber());
    }

}
