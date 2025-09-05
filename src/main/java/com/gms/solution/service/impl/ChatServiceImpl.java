/*
 * ChatServiceImpl.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service.impl;

import com.gms.solution.model.dto.ChatMessageDTO;
import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.ChatRepository;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveMessage(ChatMessageDTO chatMessageDto) {
        Message message = new Message();

        User sender = null;
        if (!"admin".equalsIgnoreCase(chatMessageDto.getSender())) {
            sender = userRepository.findByEmail(chatMessageDto.getSender());
        }

        User receiver = null;
        if (!"admin".equalsIgnoreCase(chatMessageDto.getReceiver())) {
            receiver = userRepository.findByUsername(chatMessageDto.getReceiver());
        }

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDto.getContent());
        message.setSentAt(chatMessageDto.getSentAt() != null
                ? chatMessageDto.getSentAt()
                : LocalDateTime.now());
        message.setIsRead(false);

        messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }

    public List<Message> getMessagesByUser(User user) {
        return messageRepository.findByReceiver(user);
    }
}
