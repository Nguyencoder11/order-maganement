/*
 * ChatMessageDTO.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ChatMessageDTO.java
 *
 * @author Nguyen
 */
@Getter
@Setter
public class ChatMessageDTO {
    private String sender;
    private String receiver;
    private String content;
}
