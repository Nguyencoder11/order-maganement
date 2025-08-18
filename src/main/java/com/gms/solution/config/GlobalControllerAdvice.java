/*
 * GlobalControllerAdvice.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

/**
 * GlobalControllerAdvice.java
 *
 * @author Nguyen
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("loggedInUser")
    public Object addUserToModel(HttpSession session) {
        return session.getAttribute("loggedInUser");
    }
}
