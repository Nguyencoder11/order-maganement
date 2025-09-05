/*
 * ProductServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gms.solution.model.entity.Category;
import com.gms.solution.model.entity.Product;
import com.gms.solution.repository.CategoryRepository;
import com.gms.solution.repository.ProductRepository;
import com.gms.solution.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ProductServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional
    @Override
    public void createProduct(Product product, MultipartFile file) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + product.getCategory().getId()));
            product.setCategory(category);
        }

        try {
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("folder", "products"));

                String url = (String) uploadResult.get("secure_url");
                product.setImagePath(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Log để debug
        System.out.println("=== Creating product ===");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getName());
        System.out.println("Price: " + product.getPrice());
        System.out.println("Stock: " + product.getStock());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "null"));
        System.out.println("ImagePath: " + product.getImagePath());

        productRepository.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(Long id, Product product, MultipartFile file) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getId()));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + product.getCategory().getId()));
            existingProduct.setCategory(category);
        }

        try {
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("folder", "products"));

                String url = (String) uploadResult.get("secure_url");
                existingProduct.setImagePath(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Log để debug
        System.out.println("=== Updating product ===");
        System.out.println("ID: " + existingProduct.getId());
        System.out.println("Name: " + existingProduct.getName());
        System.out.println("Price: " + existingProduct.getPrice());
        System.out.println("Stock: " + existingProduct.getStock());
        System.out.println("Description: " + existingProduct.getDescription());
        System.out.println("Category: " + (existingProduct.getCategory() != null ? existingProduct.getCategory().getName()
                                                                                 : "null"));
        System.out.println("ImagePath: " + existingProduct.getImagePath());

        productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> filterProducts(Long categoryId, String priceRange) {
        BigDecimal min_price = null;
        BigDecimal max_price = null;

        if (priceRange != null
                && !priceRange.isEmpty()) {
            String[] parts = priceRange.split("-");
            if (!parts[0].isEmpty()) min_price = new BigDecimal(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) max_price = new BigDecimal(parts[1]);
        }

        return productRepository.filterProducts(categoryId, min_price, max_price);
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }

        return productRepository.searchByKeyword(keyword);
    }

}
