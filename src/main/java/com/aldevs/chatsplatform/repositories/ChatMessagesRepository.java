package com.aldevs.chatsplatform.repositories;

import com.aldevs.chatsplatform.entity.ChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatTextMessage, Long> {
    List<ChatTextMessage> findAllByChatUUID(String chatUUID);
    Optional<ChatTextMessage> findByChatUUIDAndMessageUUID(String chatUUID, String messageUUID);
    //List<ChatTextMessage> find
}
