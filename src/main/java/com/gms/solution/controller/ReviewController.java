/*
 * ReviewController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.service.ICartService;
import com.gms.solution.service.IProductService;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ReviewController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/evaluate")
public class ReviewController {
    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICartService cartService;

    @PostMapping("/product/{id}")
    public String evaluateProduct(@PathVariable Long id) {
        return "";
    }
}
