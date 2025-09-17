/*
 * ICartService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.*;

import java.util.List;

/**
 * ICartService.java
 *
 * @author Nguyen
 */
public interface ICartService {
    Cart getOrCreateCart(User user);
    void addToCart(User user, Product product);
    int getTotalQuantity(User user);

    List<CartItems> getCartItems(User user);
    Cart getCartByUser(User user);

    void updateQuantity(User user, Product product, int quantity);
    void deleteCartItem(User user, Long productId);
    void clearCart(User user);
}
