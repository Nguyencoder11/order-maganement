/*
 * IUserService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * IUserService.java
 *
 * @author Nguyen
 */
public interface IUserService {
    User register(User user);
    boolean existByEmail(String email);
    boolean existByUsername(String username);
    User login(String username, String password);
    User findByUsername(String username);
    void changePassword(String username, String newPassword);
    List<User> getAllUsers();

    User findByUserId(Long id);

    void updateUserInfo(Long id, User user, MultipartFile file);
}
