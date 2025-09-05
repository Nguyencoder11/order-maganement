/*
 * StompPricipal.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.websocket;

import java.security.Principal;

/**
 * StompPricipal.java
 *
 * @author Nguyen
 */
public class StompPrincipal implements Principal {
    private String username;

    public StompPrincipal() {

    }

    public StompPrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
