/*
 * UserServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gms.solution.enums.RoleName;
import com.gms.solution.model.dto.UserWithLastMessage;
import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.Role;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.ChatRepository;
import com.gms.solution.repository.RoleRepository;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ChatRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
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
            user.setPassword(passwordEncoder.encode(newPassword));
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
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                            ObjectUtils.asMap("folder", "users"));

                    String url = (String) uploadResult.get("secure_url");
                    userExisted.setImagePath(url);
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

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Lấy tin nhắn mới nhất người dùng gửi tới
    @Override
    public List<UserWithLastMessage> getAllUsersWithLastMessage() {
        List<User> users = userRepository.findAll();
        List<UserWithLastMessage> result = new ArrayList<>();

        for (User u : users) {
            Message lastMsg = messageRepository.findLastMessage(u.getId(), PageRequest.of(0,1))
                    .stream()
                    .findFirst()
                    .orElse(null);

            boolean hasUnread = false;
            String displayMessage = "";
            
            if (lastMsg != null) {
                // Kiểm tra tin nhắn chưa đọc: user gửi cho admin (sender != null, receiver == null) và chưa đọc
                if (lastMsg.getSender() != null && lastMsg.getReceiver() == null && Boolean.FALSE.equals(lastMsg.getIsRead())) {
                    hasUnread = true;
                }
                
                // Tạo message hiển thị với prefix phù hợp
                if (lastMsg.getSender() == null) {
                    // Admin gửi cho user
                    displayMessage = "Me: " + lastMsg.getContent();
                } else {
                    // User gửi cho admin
                    displayMessage = lastMsg.getContent();
                }
                
                System.out.println("=== USER WITH LAST MESSAGE ===");
                System.out.println("User: " + u.getUsername());
                System.out.println("Last Message: " + displayMessage);
                System.out.println("Sender: " + (lastMsg.getSender() != null ? lastMsg.getSender().getUsername() : "admin"));
                System.out.println("Receiver: " + (lastMsg.getReceiver() != null ? lastMsg.getReceiver().getUsername() : "admin"));
                System.out.println("IsRead: " + lastMsg.getIsRead());
                System.out.println("HasUnread: " + hasUnread);
                System.out.println("SentAt: " + lastMsg.getSentAt());
            }
            System.out.println("User: " + u.getUsername() + ", hasUnread: " + hasUnread);

            result.add(new UserWithLastMessage(
                    u.getId(),
                    u.getUsername(),
                    u.getImagePath(),
                    displayMessage,
                    hasUnread
            ));
        }

        return result;
    }
}
