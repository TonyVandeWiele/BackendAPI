package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.SAVDTO;
import com.hepl.backendapi.entity.dbservices.SAVEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SAVMapper {

    SAVDTO toDTO(SAVEntity savEntity);

    SAVEntity toEntity(SAVDTO savDTO);
}

