/*
 * ChatMessageDTO.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.model.dto;

import com.gms.solution.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ChatMessageDTO.java
 *
 * @author Nguyen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime sentAt;
}
