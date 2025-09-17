/*
 * IOrderService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.CartItems;
import com.gms.solution.model.entity.Order;
import com.gms.solution.model.entity.OrderItems;
import com.gms.solution.model.entity.User;

import java.util.List;

/**
 * IOrderService.java
 *
 * @author Nguyen
 */
public interface IOrderService {
    Order createOrder(Long userId, List<OrderItems> items);
}
