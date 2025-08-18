/*
 * CartController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * CartController.java
 *
 * @author Nguyen
 */
@Controller
public class CartController {



    // Hien thi noi dung mainContent cho phan gio hang voi duong dan /cart
    @GetMapping("/cart")
    public ModelAndView cart() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("mainContent", "users/cart");
        mav.addObject("pageTitle", "Giỏ hàng");
        return mav;
    }



}
