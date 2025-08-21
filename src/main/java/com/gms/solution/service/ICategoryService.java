/*
 * ICategoryService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.Category;

import java.util.List;

/**
 * ICategoryService.java
 *
 * @author Nguyen
 */
public interface ICategoryService {
    List<Category> getAllCategories();
    List<Category> getAllCategoryName();
}
