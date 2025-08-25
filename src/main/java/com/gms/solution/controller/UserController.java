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
import org.springframework.web.multipart.MultipartFile;
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

        return "redirect:/auth/login";
    }

    // Xem thong tin tai khoan
    @GetMapping("/users/profile/{id}")
    public ModelAndView userProfile(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("mainContent", "users/profile");
        mav.addObject("pageTitle", "Profile");

        User userExisted = userService.findByUserId(id);
        mav.addObject("user", userExisted);
        return mav;
    }

    // Hien thi form chinh sua tai khoan
    @GetMapping("/users/profile/{id}/update")
    public ModelAndView updateProfileView(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("mainContent", "users/update-profile");
        mav.addObject("pageTitle", "Profile");
        mav.addObject("user", userService.findByUserId(id));
        return mav;
    }

    // Xu ly cap nhat profile
    @PostMapping("/users/profile/{id}/update")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute("user") User user,
                                @RequestParam(value = "file", required = false) MultipartFile file
                                ) {
        user.setId(id);
        userService.updateUserInfo(id, user, file);

        return "redirect:/users/profile/" + id;
    }

    // Quen mat khau
    @GetMapping("/forget-password")
    public String forgetPasswordPage(Model model) {
        model.addAttribute("username", "");
        return "users/forget-password";
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestParam("username") String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Tên người dùng không tồn tại");
            return "users/forget-password";
        }
        model.addAttribute("username", username);
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("new-password") String newPassword,
                                @RequestParam("confirm-new-password") String confirmNewPassword,
                                Model model) {
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            model.addAttribute("username", username);
            return "users/reset-password";
        }

        userService.changePassword(username, newPassword);
        model.addAttribute("message", "Mật khẩu đã được thay đổi thành công. Bạn có thể đăng nhập ngay");
        return "redirect:/auth/login";
    }



}
