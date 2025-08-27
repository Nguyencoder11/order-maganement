/*
 * CartItemsRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.repository;

import com.gms.solution.model.entity.Cart;
import com.gms.solution.model.entity.CartItems;
import com.gms.solution.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CartItemsRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    Optional<CartItems> findByCartAndProduct(Cart cart, Product product);
    List<CartItems> findByCart(Cart cart);
}
