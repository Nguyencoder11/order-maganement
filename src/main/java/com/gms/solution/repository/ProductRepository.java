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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Query("SELECT p FROM Product p JOIN FETCH p.category c " +
            "WHERE (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "ORDER BY p.updatedAt DESC")
    List<Product> filterProducts(@Param("categoryId") Long categoryId,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);
}
