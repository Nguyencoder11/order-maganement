/*
 * AdminController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.dto.UserWithLastMessage;
import com.gms.solution.model.entity.Category;
import com.gms.solution.model.entity.Product;
import com.gms.solution.model.entity.User;
import com.gms.solution.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private IChatService chatService;


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
                List<UserWithLastMessage> listUsers = userService.getAllUsersWithLastMessage();
                mav.addObject("listUsers", listUsers);
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
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/update-product");

        Product product = productService.getProductById(id);
        if (product.getPrice() != null) {
            product.setPrice(product.getPrice().setScale(0, RoundingMode.DOWN));
        }
        List<Category> categories  = categoryService.getAllCategoryName();

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


    // Xu ly xoa san pham
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }

    // Xy ly loc san pham
    @GetMapping("/products/filter")
    public ModelAndView filterProduct(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String priceRange) {
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/products");

        List<Product> products = productService.filterProducts(categoryId, priceRange);
        List<Category> categories = categoryService.getAllCategoryName();
        mav.addObject("products", products);
        mav.addObject("categories", categories);
        mav.addObject("categoryId", categoryId);
        mav.addObject("priceRange", priceRange);

        return mav;
    }


    /* =================================== QUAN LY NGUOI DUNG ===================================== */
    // Xu ly xoa nguoi dung
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Thay doi trang thai tai khoan cua nguoi dung (active/locked)
    @PostMapping(value = "/users/toggle-status/{id}", produces = "application/json")
    @ResponseBody
    public Map<String, Object> toggleUserStatus(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.findByUserId(id);

        if (user != null) {
            boolean current = Boolean.TRUE.equals(user.getEnabled());
            user.setEnabled(!current);
            userService.saveUser(user);
            response.put("status", "success");
            response.put("enabled", user.getEnabled());
        } else {
            response.put("status", "error");
        }

        return response;
    }


    /* =================================== CHAT ===================================== */
    @GetMapping("/chat/user/{id}")
    public ModelAndView chatUser(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("admin/admin-dashboard");
        mav.addObject("content", "admin/chat-user");
        User user = userService.findByUserId(id);
        mav.addObject("user", user);
        mav.addObject("chatUser", user.getUsername());
        mav.addObject("admin", "admin");

        chatService.markMessageAsRead(user);

        return mav;
    }

}
