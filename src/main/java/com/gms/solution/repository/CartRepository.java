/*
 * CartRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.repository;

import com.gms.solution.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CartRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
