package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.SAVDTO;
import com.hepl.backendapi.entity.dbservices.SAVEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
@Qualifier("savMapper")
public interface SAVMapper {

    SAVDTO toDTO(SAVEntity savEntity);

    SAVEntity toEntity(SAVDTO savDTO);
}

