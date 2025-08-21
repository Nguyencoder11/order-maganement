/*
 * CategoryServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.model.entity.Category;
import com.gms.solution.repository.CategoryRepository;
import com.gms.solution.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllWithProducts();
    }

    @Override
    public List<Category> getAllCategoryName() {
        return categoryRepository.getAllCategoryName();
    }
}
