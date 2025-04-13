package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.SAVEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SAVRepository extends JpaRepository<SAVEntity, Long> {
}
