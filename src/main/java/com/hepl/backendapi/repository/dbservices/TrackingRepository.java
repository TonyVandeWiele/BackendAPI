package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<TrackingEntity, Long> {
}
