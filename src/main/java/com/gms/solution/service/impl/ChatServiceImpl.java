/*
 * ChatServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.ChatRepository;
import com.gms.solution.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ChatServiceImpl.java
 *
 * @author Nguyen
 */
@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ChatRepository messageRepository;

    @Override
    public Message saveMessage(Message message) {
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
}
