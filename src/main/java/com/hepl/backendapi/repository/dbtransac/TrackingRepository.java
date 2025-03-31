package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<TrackingEntity, Long> {
}
