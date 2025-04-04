package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
