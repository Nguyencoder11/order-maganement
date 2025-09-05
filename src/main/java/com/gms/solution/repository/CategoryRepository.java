/*
 * CategoryRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.repository;

import com.gms.solution.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CategoryRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Lay ra tat ca san pham theo tung loai san pham
    @Query(name = "Category.findAllWithProducts")   // Goi lai NamedQuery
    List<Category> findAllWithProducts();

    // Lay ra ten cac loai san pham
    @Query(name = "Category.getAllCategoryName")
    List<Category> getAllCategoryName();
}
