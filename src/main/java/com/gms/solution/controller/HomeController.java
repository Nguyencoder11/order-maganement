/*
 * HomeController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.Category;
import com.gms.solution.model.entity.Product;
import com.gms.solution.service.ICategoryService;
import com.gms.solution.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * HomeController.java
 *
 * @author Nguyen
 */
@Controller
public class HomeController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    // Tao mapping chuyen huong toi duong dan /home khi chay ung dung
    @GetMapping("/")
    public String homePage() {
        return "redirect:/home";
    }

    // Duong dan /home tra ve trang index.html
    @GetMapping("/home")
    public ModelAndView home(HttpSession session) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("loggedInUser", session.getAttribute("loggedInUser"));
        // Them hien thi san pham cho layout chung
        mav.addObject("mainContent", "fragments/product");
        mav.addObject("pageTitle", "Trang chủ");

        List<Category> categories = categoryService.getAllCategories();
        mav.addObject("categories", categories);

        return mav;
    }

}
