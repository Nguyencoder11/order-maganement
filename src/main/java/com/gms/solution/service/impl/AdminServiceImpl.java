/*
 * AdminServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.service.IAdminService;
import org.springframework.stereotype.Service;

/**
 * AdminServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class AdminServiceImpl implements IAdminService {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    public boolean login(String username, String password) {
        return ADMIN_USER.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
