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

import java.security.Principal;
import java.time.LocalDateTime;

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
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessageDTO,
                               Principal principal) {
        // Lấy tên người gửi từ Principal (nếu có) hoặc từ payload
        String senderName = principal != null ? principal.getName()
                                              : chatMessageDTO.getSender();

        String receiverName = chatMessageDTO.getReceiver();
        System.out.println("WS: " + senderName + " -> " + receiverName + ": " + chatMessageDTO.getContent());
        simpMessagingTemplate.convertAndSendToUser(
                receiverName,
                "/queue/messages",
                new ChatMessageDTO(
                        senderName,
                        receiverName,
                        chatMessageDTO.getContent(),
                        LocalDateTime.now()
                )
        );


        chatMessageDTO.setSender(senderName);
        chatMessageDTO.setSentAt(LocalDateTime.now());
        chatService.saveMessage(chatMessageDTO);
    }
}
