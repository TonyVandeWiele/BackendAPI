package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
     Optional<UserEntity> findByClientAccountNumber(String clientAccountNumber);
}
