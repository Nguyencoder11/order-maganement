/*
 * AdminController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * AdminController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @GetMapping("/login")
    public String adminLogin() {
        return "admin/login-admin";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,  // Luu session dang nhap cua admin
                               Model model
                               ) {
        if (ADMIN_USER.equals(username) && ADMIN_PASSWORD.equals(password)) {
            session.setAttribute("adminUser", username);

            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "admin/login-admin";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        return "admin/dashboard";
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


    @GetMapping("/products")
    public ModelAndView showProductsTab() {
        ModelAndView mav = new ModelAndView("admin/dashboard");
        mav.addObject("pageContent", "admin/products");
        return mav;
    }

    @GetMapping("/users")
    public ModelAndView showUsersTab() {
        ModelAndView mav = new ModelAndView("admin/dashboard");
        mav.addObject("pageContent", "admin/users");
        return mav;
    }

    @GetMapping("/chat")
    public ModelAndView showChatTab() {
        ModelAndView mav = new ModelAndView("admin/dashboard");
        mav.addObject("pageContent", "admin/chat");
        return mav;
    }

    @GetMapping("/products/create")
    public ModelAndView showCreateProductWindow() {
        ModelAndView mav = new ModelAndView("admin/dashboard");
        mav.addObject("pageContent", "admin/create-product");
        return mav;
    }
}
