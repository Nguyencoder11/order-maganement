/*
 * ChatController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.controller;

import com.gms.solution.model.dto.ChatMessageDTO;
import com.gms.solution.model.dto.UserWithLastMessage;
import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import com.gms.solution.repository.UserRepository;
import com.gms.solution.service.IChatService;
import com.gms.solution.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserService userService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
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

        List<UserWithLastMessage> updatedList = userService.getAllUsersWithLastMessage();
        simpMessagingTemplate.convertAndSend("/topic/chat-list-update", updatedList);

        System.out.println("Principal: " + principal.getName());
        System.out.println("Receiver: " + receiverName);

        chatMessageDTO.setSender(senderName);
        chatMessageDTO.setSentAt(LocalDateTime.now());
        chatService.saveMessage(chatMessageDTO);
    }

    @GetMapping("/chat/history/{username}")
    @ResponseBody
    public List<ChatMessageDTO> getHistoryChat(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        List<Message> messages = chatService.getChatHistoryWithAdmin(user);

        return messages.stream().map(m -> new ChatMessageDTO(
                m.getSender() != null ? m.getSender().getUsername() : "admin",
                m.getReceiver() != null ? m.getReceiver().getUsername() : "admin",
                m.getContent(),
                m.getSentAt()
        )).toList();
    }
}
