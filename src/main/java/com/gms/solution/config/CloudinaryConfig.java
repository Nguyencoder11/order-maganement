/*
 * CloudinaryConfig.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CloudinaryConfig.java
 *
 * @author Nguyen
 */
@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhe0ysmoy",
                "api_key", "723618613293544",
                "api_secret", "OFpnCSqLg8oDL2jrfDEa6vm90po",
                "secure", true   // luôn dùng HTTPS
        ));
    }
}
