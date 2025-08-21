/*
 * AdminController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.Category;
import com.gms.solution.model.entity.Product;
import com.gms.solution.model.entity.User;
import com.gms.solution.service.IAdminService;
import com.gms.solution.service.ICategoryService;
import com.gms.solution.service.IProductService;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.model.IModel;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String SESSION_ADMIN = "adminUser";

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IUserService userService;

    // Hien thi trang login cua admin
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/login-admin";
    }

    // Xu ly dang nhap tai khoan cua admin
    @PostMapping("/login")
    public String adminLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,  // Luu session dang nhap cua admin
                               Model model
                               ) {
        if (adminService.login(username, password)) {
            session.setAttribute(SESSION_ADMIN, username);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "admin/login-admin";
        }
    }

//    // Lay danh sach nguoi dung
//    @GetMapping("/admin/users")
//    public ModelAndView getAllUsersByAdmin() {
//        return new ModelAndView();
//    }
//
//    // Xoa nguoi dung
//    @DeleteMapping("/admin/users/{id}")
//    public ModelAndView deleteUserByAdmin(@PathVariable int id) {
//        return new ModelAndView();
//    }

    @GetMapping("/{tab}")
    public ModelAndView showAdminTab(@PathVariable String tab, HttpSession session) {
        if (session.getAttribute(SESSION_ADMIN) == null) {
            return new ModelAndView("redirect:/admin/login");
        }

        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        switch (tab) {
            case "dashboard":
                mav.addObject("content", "admin/home");
                break;
            case "categories":
                mav.addObject("content", "admin/categories");
                break;
            case "products":
                List<Category> categories = categoryService.getAllCategoryName();
                List<Product> products = productService.getAllProducts();
                mav.addObject("content", "admin/products");
                mav.addObject("categories", categories);
                mav.addObject("products", products);
                System.out.println("Total products: " + products.size());
                break;
            case "chat":
                mav.addObject("content", "admin/chat");
                break;
            case "users":
                List<User> users = userService.getAllUsers();
                mav.addObject("content", "admin/users");
                mav.addObject("users", users);
                System.out.println("Total users: " + users.size());
                break;
            default:
                mav.addObject("content", "admin/home");
                break;
        }

        return mav;
    }

    @GetMapping("/products/create")
    public ModelAndView createProductView() {
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/create-product");
        return mav;
    }

    @GetMapping("/products/update/{id}")
    public ModelAndView updateProductView(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        List<Category> categories  = categoryService.getAllCategoryName();
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/update-product");
        mav.addObject("product", product);
        mav.addObject("categories", categories);
        return mav;
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@ModelAttribute("product") Product product) {
        productService.updateProduct(product);
        return "redirect:/admin/products";
    }

}
