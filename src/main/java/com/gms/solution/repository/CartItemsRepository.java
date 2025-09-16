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
import com.gms.solution.model.entity.User;
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
    // Lay thong tin san pham trong gio hang
    Optional<CartItems> findByCartAndProduct(Cart cart, Product product);

    // Lay danh sach cac san pham trong gio hang
    List<CartItems> findByCart(Cart cart);

    // Lay san pham tu gio hang theo nguoi dung va productID
    CartItems findByCart_UserAndProductId(User user, Long productId);
}
