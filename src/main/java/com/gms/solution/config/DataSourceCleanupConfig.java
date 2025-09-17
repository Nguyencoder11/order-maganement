/*
 * DataSourceCleanupConfig.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.config;

import org.springframework.context.annotation.Configuration;
import javax.annotation.PreDestroy;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * DataSourceCleanupConfig.java
 *
 * @author Nguyen
 */
@Configuration
public class DataSourceCleanupConfig {
    @PreDestroy
    public void cleanup() throws Exception {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            DriverManager.deregisterDriver(driver);
        }

        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
