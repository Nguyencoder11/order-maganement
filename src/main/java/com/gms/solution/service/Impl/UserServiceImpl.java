///*
// * UserServiceImpl.java
// *
// * Copyright (c) 2025 Nguyen. All rights reserved.
// * This software is the confidential and proprietary information of Nguyen.
// */
//
//package com.gms.solution.service.Impl;
//
//import com.gms.solution.model.entity.User;
//import com.gms.solution.repository.UserRepository;
//import com.gms.solution.service.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * UserServiceImpl.java
// *
// * @author Nguyen
// */
//@Service
//public class UserServiceImpl implements IUserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Dang ky tai khoan
//    @Override
//    public User register(User user) {
//        if (userRepository.findByEmail(user.getEmail()) != null ||
//        userRepository.findByUsername(user.getUsername()) != null) {
//            throw new RuntimeException("User already exists");
//        }
//
//        user.setEnabled(true);
//        return userRepository.save(user);
//    }
//}
