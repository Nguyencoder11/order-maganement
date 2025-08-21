/*
 * AuthController.java
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * AuthController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    // Hien thi trang login cua nguoi dung
    @GetMapping("/login")
    public ModelAndView userLoginPage(Model model) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/login")
    public String userLogin(@ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model,
                                  HttpSession session) {
        if (!user.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            model.addAttribute("error", "Tên đăng nhập không chứa ký tự đặc biệt");
            return "login";
        }

        if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            return "login";
        }

        try {
            User userLogin = userService.login(user.getUsername(), user.getPassword());
            if (userLogin != null) {
                session.setAttribute("loggedInUser", userLogin);
            } else {
                model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
                return "login";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }

        return "redirect:/home";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
