package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByCategoryName(String categoryName);
}
