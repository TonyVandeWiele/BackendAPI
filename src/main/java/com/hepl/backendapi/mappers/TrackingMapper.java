package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.TrackingDTO;
import com.hepl.backendapi.entity.dbtransac.TrackingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackingMapper {
    TrackingDTO toTrackingDTO(TrackingEntity trackingEntity);
    TrackingEntity toTrackingEntity(TrackingDTO trackingDTO);
}
