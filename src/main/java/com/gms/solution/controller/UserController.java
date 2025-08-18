/*
 * UserController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.User;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * UserController.java
 *
 * @author Nguyen
 */
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public ModelAndView registerPage() {
        ModelAndView view = new ModelAndView("register");
        view.addObject("user", new User());
        return view;
    }

    // Dang ky nguoi dung
    @PostMapping("/register")
    public String userRegister(@ModelAttribute("user") User user,
                               BindingResult result,
                               Model model,
                               @RequestParam("confirm-password") String confirmPassword) {

        if (!user.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            model.addAttribute("error", "Tên đăng nhập không chứa ký tự đặc biệt");
            return "register";
        }

        if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            return "register";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu nhập lại không trùng khớp");
            return "register";
        }

        if (userService.existByUsername(user.getUsername())) {
            model.addAttribute("error", "Tên đăng nhập này đã tồn tại");
            return "register";
        }

        if (userService.existByEmail(user.getEmail())) {
            model.addAttribute("error", "Email này đã được đăng ký trước đó");
            return "register";
        }

        try {
            userService.register(user);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

    // Xem thong tin tai khoan
    @GetMapping("/users/profile")
    public ModelAndView userProfile() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("mainContent", "users/profile");
        mav.addObject("pageTitle", "Profile");
        return mav;
    }

    // Thay doi mat khau
    @GetMapping("/users/reset-password")
    public ModelAndView resetPasswordPage() {
        ModelAndView mav = new ModelAndView("users/reset-password");
        return mav;
    }

}
