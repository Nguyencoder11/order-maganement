/*
 * ProductRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.repository;

import com.gms.solution.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p join fetch p.category order by p.updatedAt desc")
    List<Product> getAllProducts();
}
