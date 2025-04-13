package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // Méthode pour récupérer tous les commentaires d'un produit par productId
    List<CommentEntity> findByProductId(Long productId);
}

