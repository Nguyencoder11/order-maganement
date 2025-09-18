/*
 * ChatRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.repository;

import com.gms.solution.model.entity.Message;
import com.gms.solution.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ChatRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface ChatRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);

    @Query("select m from Message m left join fetch m.sender "
            + "where (m.sender.id = :userId and m.receiver.id is null) "
            + " or (m.sender.id is null and m.receiver.id = :userId) "
            + "order by m.sentAt ASC ")
    List<Message> findChatHistory(@Param("userId") Long userId);

    @Query("select m from Message m " +
            "WHERE (m.sender.id = :userId AND m.receiver IS NULL) " +
            "   OR (m.receiver.id = :userId AND m.sender IS NULL) " +
            "ORDER BY m.sentAt DESC ")
    List<Message> findLastMessage(@Param("userId") Long id, Pageable pageable);
}
