/*
 * CartServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.model.entity.Cart;
import com.gms.solution.model.entity.CartItems;
import com.gms.solution.model.entity.Product;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.CartItemsRepository;
import com.gms.solution.repository.CartRepository;
import com.gms.solution.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CartServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;


    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setCreatedAt(LocalDateTime.now());
                    cart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    @Override
    public void addToCart(User user, Product product) {
        Cart cart = getOrCreateCart(user);

        CartItems cartItem = cartItemsRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItems ci = new CartItems();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    ci.setPrice(product.getPrice());
                    ci.setCreatedAt(LocalDateTime.now());
                    return ci;
                });
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItemsRepository.save(cartItem);

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public int getTotalQuantity(User user) {
        return cartRepository.findByUser(user)
                .map(cart -> cartItemsRepository.findByCart(cart)
                        .stream()
                        .mapToInt(CartItems::getQuantity)
                        .sum())
                .orElse(0);
    }

    @Override
    public List<CartItems> getCartItems(User user) {
        Cart cart = getOrCreateCart(user);
        return cartItemsRepository.findByCart(cart)
                .stream()
                .filter(ci -> ci.getQuantity() > 0)
                .toList();
    }

    @Override
    public Cart getCartByUser(User user) {
        return getOrCreateCart(user);
    }

    @Override
    public void updateQuantity(User user, Product product, int quantity) {
        CartItems item = cartItemsRepository.findByCart_UserAndProductId(user, product.getId());
        if (item != null) {
            item.setQuantity(quantity);
            item.setUpdatedAt(LocalDateTime.now());
            cartItemsRepository.save(item);
        }
    }

    @Override
    public void deleteCartItem(User user, Long productId) {
        CartItems item = cartItemsRepository.findByCart_UserAndProductId(user, productId);
        if (item != null) {
            cartItemsRepository.delete(item);

            Cart cart = item.getCart();
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartItemsRepository.deleteByCart_User(user);
    }
}
