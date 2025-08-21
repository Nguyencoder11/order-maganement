/*
 * IProductService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.Product;

import java.util.List;

/**
 * IProductService.java
 *
 * @author Nguyen
 */
public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product updateProduct(Product product);
}
