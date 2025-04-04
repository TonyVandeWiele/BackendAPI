package com.hepl.backendapi.repository.dbtransac;

import com.hepl.backendapi.entity.dbtransac.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
