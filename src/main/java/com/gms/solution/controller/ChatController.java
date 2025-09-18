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
        System.out.println("=== CHAT MESSAGE RECEIVED ===");
        System.out.println("Sender: " + senderName);
        System.out.println("Receiver: " + receiverName);
        System.out.println("Content: " + chatMessageDTO.getContent());
        System.out.println("Principal: " + (principal != null ? principal.getName() : "null"));
        
        // Gửi tin nhắn đến người nhận cụ thể
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

        // Lưu tin nhắn vào database
        chatMessageDTO.setSender(senderName);
        chatMessageDTO.setSentAt(LocalDateTime.now());
        chatService.saveMessage(chatMessageDTO);

        // Cập nhật danh sách chat real-time cho tất cả admin
        List<UserWithLastMessage> updatedList = userService.getAllUsersWithLastMessage();
        
        System.out.println("=== SENDING REAL-TIME UPDATE ===");
        System.out.println("Updated list size: " + updatedList.size());
        for (UserWithLastMessage user : updatedList) {
            if (user.isHasUnread()) {
                System.out.println("User with unread: " + user.getUsername() + " - " + user.getLastMessage());
            }
        }
        
        // Gửi cập nhật đến topic chung cho tất cả admin
        simpMessagingTemplate.convertAndSend("/topic/chat-list-update", updatedList);
        System.out.println("Sent update to /topic/chat-list-update");
        
        // Gửi cập nhật đến user destination cho admin cụ thể (nếu cần)
        if ("admin".equalsIgnoreCase(receiverName)) {
            simpMessagingTemplate.convertAndSendToUser("admin", "/queue/chat-list-update", updatedList);
            System.out.println("Sent update to /user/admin/queue/chat-list-update");
        }
        
        // Luôn gửi cập nhật đến user destination cho admin (để đảm bảo)
        simpMessagingTemplate.convertAndSendToUser("admin", "/queue/chat-list-update", updatedList);
        System.out.println("Sent update to /user/admin/queue/chat-list-update (always)");
        
        // Gửi cập nhật đến tất cả admin đang online
        simpMessagingTemplate.convertAndSend("/topic/admin-chat-update", updatedList);
        System.out.println("Sent update to /topic/admin-chat-update");

        System.out.println("Principal: " + (principal != null ? principal.getName() : "null"));
        System.out.println("Receiver: " + receiverName);
        System.out.println("Updated chat list sent to all admins");
        System.out.println("Chat list size: " + updatedList.size());
        
        // Debug: In ra thông tin tin nhắn chưa đọc
        for (UserWithLastMessage user : updatedList) {
            if (user.isHasUnread()) {
                System.out.println("User " + user.getUsername() + " has unread message: " + user.getLastMessage());
            }
        }
    }

    @GetMapping(value = "/chat/history/{username}", produces = "application/json; charset=utf-8")
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
