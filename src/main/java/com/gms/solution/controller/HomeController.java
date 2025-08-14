/*
 * HomeController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * HomeController.java
 *
 * @author Nguyen
 */
@Controller
public class HomeController {
    // Tao mapping chuyen huong toi duong dan /home khi chay ung dung
    @GetMapping("/")
    public String homePage() {
        return "redirect:/home";
    }

    // Duong dan /home tra ve trang index.html
    @GetMapping("/home")
    public String home(){
        return "index";
    }
}
