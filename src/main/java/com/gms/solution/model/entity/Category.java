/*
 * Category.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Category.java
 *
 * @author Nguyen
 */
// NamedQuery lay danh sach san pham theo danh muc
@NamedQuery(
        name = "Category.findAllWithProducts",
        query = "SELECT distinct c from Category c left join fetch c.products"
)
@NamedQuery(
        name = "Category.getAllCategoryName",
        query = "SELECT c from Category c order by c.name asc"
)
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "category_name")
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnoreProperties({"category"})
    private List<Product> products = new ArrayList<>();
}
