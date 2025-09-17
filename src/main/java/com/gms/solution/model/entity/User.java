/*
 * User.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User.java
 *
 * @author Nguyen
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)     // user luon can role -> EAGER
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"users"})    // tranh vong lap JSON
    Role role;

    @Column(name = "username")
    String username;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "full_name")
    @JsonIgnore     // bo qua de tranh leak password
    String fullName;

    @Column(name = "gender")
    String gender;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Column(name = "bio")
    String bio;

    @Column(name = "image_path")
    String imagePath;

    @Column(name = "phone")
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "is_active")
    Boolean enabled;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}


