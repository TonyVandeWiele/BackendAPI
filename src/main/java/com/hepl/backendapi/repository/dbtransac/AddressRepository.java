package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByNumberAndStreetAndCityAndZipCodeAndCountry(
            String number, String street, String city, String zipCode, String country);
    @Query("SELECT DISTINCT o.address.id FROM OrderEntity o WHERE o.clientId = :clientId")
    List<Long> findAddressIdsByClientId(@Param("clientId") Long clientId);
}
