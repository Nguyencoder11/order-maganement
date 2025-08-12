/*
 * LoginController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LoginController.java
 *
 * @author Nguyen
 */
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
