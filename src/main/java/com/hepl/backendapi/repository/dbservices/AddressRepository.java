package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByStreetAndCityAndZipCodeAndCountry(String street, String city, String zipCode, String country);
}
