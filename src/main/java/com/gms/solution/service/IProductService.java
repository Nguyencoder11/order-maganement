/*
 * IProductService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * IProductService.java
 *
 * @author Nguyen
 */
public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void createProduct(Product product, MultipartFile file);
    void updateProduct(Long id, Product product, MultipartFile imagePath);
    void deleteProduct(Long id);
}
