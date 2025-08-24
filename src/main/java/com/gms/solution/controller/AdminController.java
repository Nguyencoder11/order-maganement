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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.RoundingMode;
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
    /**
     * Endpoint for admin login.
     * @return 'admin/login-admin.html' as a view for admin login view
     */
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/login-admin";
    }

    // Xu ly dang nhap tai khoan cua admin
    /**
     * Endpoint for processing login admin
     * @Param username for admin account
     * @Param password for admin password
     * @Param session to save admin login session
     * @Param model to add attribute "error"
     * @return 'redirect:/admin/dashboard' as a dashboard view if success, else stay in login view
     */
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

    // Lay thong tin noi dung theo cac tab menu cu the cho admin
    /**
     * Endpoint for admin dashboard fragment tab view.
     * @PathVariable tab for get fragment tab view
     * @Param session for save admin login session
     * @return fragment tab views
     */
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

    /* =================================== QUAN LY SAN PHAM ===================================== */
    // Hien thi form view tao san pham
    /**
     * Endpoint for 'create-product' fragment form view.
     * @return 'create-product.html' fragment form as a view
     */
    @GetMapping("/products/create")
    public ModelAndView createProductView() {
        List<Category> categories  = categoryService.getAllCategoryName();
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/create-product");
        mav.addObject("product", new Product());
        mav.addObject("categories", categories);
        return mav;
    }

    // Xu ly them moi san pham
    /**
     * Endpoint for 'create-product' fragment form view. Post and create product
     * @return
     */
    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute("product") Product product,
                                @RequestParam("file") MultipartFile file) {

        productService.createProduct(product, file);
        return "redirect:/admin/products";
    }

    // Hien thi form view update san pham
    /**
     * Endpoint for 'update-product' fragment form view.
     * @PathVariable id for get product info by ID from DB
     * @return 'update-product.html' fragment form as a view with product info by id and category select
     */
    @GetMapping("/products/update/{id}")
    public ModelAndView updateProductView(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product.getPrice() != null) {
            product.setPrice(product.getPrice().setScale(0, RoundingMode.DOWN));
        }
        List<Category> categories  = categoryService.getAllCategoryName();
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/update-product");
        mav.addObject("product", product);
        mav.addObject("categories", categories);
        return mav;
    }

    // Xy lu update san pham
    /**
     * Endpoint for 'update-product' fragment form view. Post form and update product info
     * @PathVariable id for get product info by ID from DB
     * @ModelAttribute "product" for object 'product' from update form
     * @Param "file" require or not multipart file for upload image
     * @return 'redirect:/admin/products' view as a view with list product info
     */
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam(value = "file", required = false) MultipartFile file
                                ) {

        productService.updateProduct(id, product, file);

        return "redirect:/admin/products";
    }

}
