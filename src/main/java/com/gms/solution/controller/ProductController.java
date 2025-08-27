/*
 * ProductController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * ProductController.java
 *
 * @author Nguyen
 */
@Controller
public class ProductController {

    @GetMapping("/search?{search}")
    public ModelAndView search(@PathVariable String search) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("pageTitle", "Search for " + search);
        mav.addObject("mainContent", "search");
        mav.addObject("search", search);
        return mav;
    }

    // Tim kiem san pham
    @PostMapping("/search?={search}")
    public String searchProduct(@RequestParam("search") String search) {


        return "";
    }
}
