package com.hepl.backendapi.repository.dbservices;

import com.hepl.backendapi.entity.dbservices.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
