/*
 * GlobalControllerAdvice.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.config;

import com.gms.solution.model.entity.User;
import com.gms.solution.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

/**
 * GlobalControllerAdvice.java
 *
 * @author Nguyen
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ICartService cartService;

    @ModelAttribute("cartItemCount")
    public int getCartItemCount(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return 0;
        return cartService.getTotalQuantity(loggedInUser);
    }

    @ModelAttribute("loggedInUser")
    public Object addUserToModel(HttpSession session) {
        return session.getAttribute("loggedInUser");
    }
}
