package com.hepl.backendapi.service;

import com.azure.core.exception.ResourceNotFoundException;
import com.azure.security.keyvault.jca.implementation.shaded.org.apache.http.protocol.HTTP;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public UserDTO updateUserCreateDTO(Long id, UserUpdateDTO userUpdateDTO) { // A changer quand JWT
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException(UserEntity.class.getSimpleName(), id));

        // Si l'adresse a été modifiée, créer une nouvelle adresse
        if (userUpdateDTO.getAddress() != null) {
            AddressEntity newAddress = addressMapper.toEntity(userUpdateDTO.getAddress());
            addressRepository.save(newAddress); // Sauvegarde la nouvelle adresse
            existingUser.setAddress(newAddress); // Associe la nouvelle adresse à l'utilisateur
        }

        // Mapping des nouveaux champs
        userMapper.updateUserFromDto(userUpdateDTO, existingUser);

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

        AddressEntity addressEntity = saveAddressIfNotExists(userCreateDTO.getAddress());
        userEntity.setAddress(addressEntity);

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
}
