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

    // Lưu tin nhắn
    @Override
    public void saveMessage(ChatMessageDTO chatMessageDto) {
        Message message = new Message();

        // Neu sender la admin -> set null
        User sender = "admin".equalsIgnoreCase(chatMessageDto.getSender())
                ? null
                : userRepository.findByUsername(chatMessageDto.getSender());


        // Neu receiver la admin -> set null
        User receiver = "admin".equalsIgnoreCase(chatMessageDto.getReceiver())
                ? null
                : userRepository.findByUsername(chatMessageDto.getReceiver());


        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDto.getContent());
        message.setSentAt(chatMessageDto.getSentAt() != null
                ? chatMessageDto.getSentAt()
                : LocalDateTime.now());
        message.setIsRead(false);

        messageRepository.save(message);

        System.out.println("Saved message: Sender=" + (sender != null ? sender.getUsername() : "admin")
                + ", Content=" + chatMessageDto.getContent()
                + ", SentAt=" + message.getSentAt());
    }

    // Lấy lịch sử tin nhắn chat của người dùng vs admin
    @Override
    public List<Message> getChatHistoryWithAdmin(User user) {
        return messageRepository.findChatHistory(user.getId());
    }

    // Đánh dấu tin nhắn đã đọc
    @Override
    public void markMessageAsRead(User user) {
        List<Message> messages = messageRepository.findBySenderAndReceiver(user, null); // user gửi cho admin
        for (Message message : messages) {
            if (!Boolean.TRUE.equals(message.getIsRead())) {
                message.setIsRead(true);
                messageRepository.save(message);
            }
        }
    }

}
