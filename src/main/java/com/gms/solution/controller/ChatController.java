/*
 * ChatController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.dto.ChatMessageDTO;
import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * ChatController.java
 *
 * @author Nguyen
 */
@Controller
public class ChatController {

    @Autowired
    private IChatService chatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessageDTO) {
        User sender = userRepository.findByUsername(chatMessageDTO.getSender());
        User receiver = userRepository.findByUsername(chatMessageDTO.getReceiver());

        if (sender != null && receiver != null) {
            return;
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(chatMessageDTO.getContent());
        chatService.saveMessage(message);

        // Gui toi nguoi nhan theo queue rieng
        simpMessagingTemplate.convertAndSendToUser(
                chatMessageDTO.getReceiver(),
                "/queue/messages",
                chatMessageDTO
        );
    }
}
