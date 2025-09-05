/*
 * IChatService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.dto.ChatMessageDTO;
import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;

import java.util.List;

/**
 * IChatService.java
 *
 * @author Nguyen
 */
public interface IChatService {
    void saveMessage(ChatMessageDTO chatMessageDto);
    List<Message> getMessages(User sender, User receiver);
}
