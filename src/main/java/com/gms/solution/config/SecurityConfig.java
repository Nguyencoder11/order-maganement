/*
 * SecurityConfig.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.config;

import com.gms.solution.enums.RoleName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig.java
 *
 * @author Nguyen
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/", "/home", "/public/**").permitAll()    // Cho truy cap khong can login
                        .antMatchers("/auth/**", "/register").hasAuthority(RoleName.ROLE_USER.name())   // Chi User ms truy cap duoc nhung endpoint nay
                        .antMatchers("/admin/**").hasAuthority(RoleName.ROLE_ADMIN.name())  // Chi admin moi duoc truy cap duoc
                        .anyRequest().authenticated())  // cac yeu cau khac yeu cau login
                .formLogin(form -> form
                        .loginPage("/auth/login")   // Trang login custom
                        .defaultSuccessUrl("/home", true)   // redirect sau khi login thanh cong
                        .permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
