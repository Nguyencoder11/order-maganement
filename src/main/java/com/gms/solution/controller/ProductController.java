/*
 * ProductController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.Product;
import com.gms.solution.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * ProductController.java
 *
 * @author Nguyen
 */
@Controller
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("keyword") String keyword) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("pageTitle", "Search for " + keyword);
        mav.addObject("mainContent", "search");
        mav.addObject("search", keyword);
        mav.addObject("products", productService.searchProducts(keyword));
        return mav;
    }

    @GetMapping("product/{id}/detail")
    public ModelAndView productDetail(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("pageTitle", "Product Detail");
        mav.addObject("mainContent", "detail");

       Product product = productService.findById(id);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        mav.addObject("product", product);
        return mav;
    }
}
