/*
 * UserServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.enums.RoleName;
import com.gms.solution.model.entity.Role;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.RoleRepository;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UserServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Dang ky tai khoan
    @Override
    @Transactional
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null ||
            userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(RoleName.ROLE_USER);
                    return roleRepository.save(newRole);
                });
        user.setRole(userRole);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUserId(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void updateUserInfo(Long id, User user, MultipartFile file) {
        User userExisted = userRepository.findById(id).orElse(null);
        if (userExisted != null) {
            userExisted.setFullName(user.getFullName());
            userExisted.setEmail(user.getEmail());
            userExisted.setPhone(user.getPhone());
            userExisted.setDateOfBirth(user.getDateOfBirth());
            userExisted.setAddress(user.getAddress());
            userExisted.setBio(user.getBio());

            try {
                if (file != null && !file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String uploadDir = "uploads/users/";
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    userExisted.setImagePath("/" + uploadDir + fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Log debug
        System.out.println("===== Updating User =====");
        System.out.println("ID: " + id);
        System.out.println("Full Name: " + userExisted.getFullName());
        System.out.println("Email: " + userExisted.getEmail());
        System.out.println("Phone: " + userExisted.getPhone());
        System.out.println("Date of birth: " + userExisted.getDateOfBirth());
        System.out.println("Address: " + userExisted.getAddress());
        System.out.println("Bio: " + userExisted.getBio());

        userRepository.save(userExisted);
    }
}
