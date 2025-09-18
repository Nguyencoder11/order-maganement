/*
 * OrderServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.enums.OrderStatus;
import com.gms.solution.model.entity.CartItems;
import com.gms.solution.model.entity.Order;
import com.gms.solution.model.entity.OrderItems;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.OrderItemsRepository;
import com.gms.solution.repository.OrderRepository;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.ICartService;
import com.gms.solution.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Order createOrder(Long userId, List<OrderItems> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not exist"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // Tinh tong tien
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        for (OrderItems item : items) {
            item.setOrder(order);
        }
        order.setItems(items);

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }
}
