///*
// * User.java
// *
// * Copyright (c) 2025 Nguyen. All rights reserved.
// * This software is the confidential and proprietary information of Nguyen.
// */
//
//package com.gms.solution.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.processing.Pattern;
//
//import java.time.LocalDateTime;
//
///**
// * User.java
// *
// * @author Nguyen
// */
//@Entity
//@Table(name = "users")
//@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @Column(name = "user_name")
//    String username;
//
//    @Column(name = "email", unique = true)
//    String email;
//
//    @Column(name = "password")
//    String password;
//
//    @Column(name = "full_name")
//    String fullName;
//
//    @Column(name = "created_at")
//    LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    LocalDateTime updatedAt = LocalDateTime.now();
//
//    @Column(name = "is_active")
//    boolean enabled = false;
//}
//
