/*
 * OrderController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.entity.*;
import com.gms.solution.service.ICartService;
import com.gms.solution.service.IOrderService;
import com.gms.solution.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OrderController.java
 *
 * @author Nguyen
 */
@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private PdfService pdfService;

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) throw new RuntimeException("User is not logged in");

        List<CartItems> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {
            OrderItems item = new OrderItems();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getPrice());
            return item;
        }).toList();

        Order order = orderService.createOrder(user.getId(), orderItems);

        byte[] pdfBytes = pdfService.generateInvoice(order);

        // Xóa giỏ hàng DB sau khi đặt hàng thành công
        cartService.clearCart(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
