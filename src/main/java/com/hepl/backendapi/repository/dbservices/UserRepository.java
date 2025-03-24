package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
