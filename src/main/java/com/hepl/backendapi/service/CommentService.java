package com.hepl.backendapi.service;

import com.hepl.backendapi.dto.generic.CommentDTO;
import com.hepl.backendapi.dto.post.CommentCreateDTO;
import com.hepl.backendapi.entity.dbservices.CommentEntity;
import com.hepl.backendapi.exception.RessourceNotFoundException;
import com.hepl.backendapi.mappers.CommentMapper;
import com.hepl.backendapi.repository.dbservices.CommentRepository;
import com.hepl.backendapi.repository.dbtransac.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final ProductRepository productRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.productRepository = productRepository;
    }

    // Créer un commentaire
    public CommentDTO createComment(CommentCreateDTO commentCreateDTO) {

        // Vérifier si le produit avec productId existe dans la base de données
        if (!productRepository.existsById(commentCreateDTO.getProductId())) {
            throw new RessourceNotFoundException("Product ID not found: " + commentCreateDTO.getProductId());
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .comment(commentCreateDTO.getComment())
                .createdAt(LocalDateTime.now())
                .rating(commentCreateDTO.getRating())
                .productId(commentCreateDTO.getProductId())
                .customerId(1L)
                .build();

        commentEntity = commentRepository.save(commentEntity);
        return commentMapper.toDTO(commentEntity);
    }

    // Récupérer tous les commentaires d'un produit
    public List<CommentDTO> getCommentsByProductId(Long productId) {
        List<CommentEntity> commentEntities = commentRepository.findByProductId(productId);
        return commentEntities.stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}

