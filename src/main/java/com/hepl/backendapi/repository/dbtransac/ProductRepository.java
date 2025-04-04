package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByCategoryName(String categoryName);
}
