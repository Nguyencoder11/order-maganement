/*
 * CartController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.CartItems;
import com.gms.solution.model.entity.Product;
import com.gms.solution.model.entity.User;
import com.gms.solution.service.ICartService;
import com.gms.solution.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * CartController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICartService cartService;

    // Hien thi noi dung mainContent cho phan gio hang voi duong dan /cart
    // Xem gio hang
    @GetMapping
    public ModelAndView cart(HttpSession session) {
        User loggedInUser = (User)  session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return new ModelAndView("redirect:/auth/login");
        }

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("pageTitle", "Giỏ hàng");
        mav.addObject("mainContent", "users/cart");

        // Lay danh sach cac san pham trong gio hang cua nguoi dung
        List<CartItems> cartItems = cartService.getCartItems(loggedInUser);
        // Tinh tong so tien gio hang
        BigDecimal total = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        mav.addObject("cartItems", cartItems);
        mav.addObject("cartTotal", total);

        return mav;
    }

    // Them san pham vao gio hang
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }

        Product product = productService.findById(productId);
        cartService.addToCart(loggedInUser, product);

        return "redirect:/home";
    }

    // Thay doi so luong san pham
    @PostMapping("/update/{productId}")
    @ResponseBody
    public BigDecimal updateCartItem(@PathVariable Long productId,
                                     @RequestBody Map<String, Integer> body,
                                     HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            throw new RuntimeException("User not logged in");
        }
        int quantity = body.get("quantity");
        Product product = productService.findById(productId);
        cartService.updateQuantity(loggedInUser, product, quantity);

        List<CartItems> cartItems = cartService.getCartItems(loggedInUser);
        return cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Xoa san pham trong gio hang
    @PostMapping("/delete/{productId}")
    public String deleteCartItem(@PathVariable Long productId,
                                 HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }

        cartService.deleteCartItem(loggedInUser, productId);
        return "redirect:/cart";
    }
}
