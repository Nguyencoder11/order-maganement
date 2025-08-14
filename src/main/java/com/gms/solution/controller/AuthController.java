/*
 * AuthController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * AuthController.java
 *
 * @author Nguyen
 */
@Controller
public class AuthController {
    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView view = new ModelAndView("login");
        return view;
    }
}
