/*
 * UserWithLastMessage.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.model.dto;

import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import lombok.*;

/**
 * UserWithLastMessage.java
 *
 * @author Nguyen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithLastMessage {
    private Long userId;
    private String username;
    private String imagePath;
    private String lastMessage;
    private boolean hasUnread;
}
