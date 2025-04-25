package com.hepl.backendapi.mappers;

import com.hepl.backendapi.dto.generic.CommentDTO;
import com.hepl.backendapi.entity.dbservices.CommentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toDTO(CommentEntity commentEntity);

    CommentEntity toEntity(CommentDTO commentDTO);
}
