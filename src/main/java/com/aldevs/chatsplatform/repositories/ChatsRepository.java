package com.aldevs.chatsplatform.repositories;

import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, Long> {

    boolean existsByTypeAndParticipantsIn(ChatType localChat, List<String> participants);
    Optional<Chat> findByTypeAndParticipantsIn(ChatType localChat, List<String> participants);
    Optional<Chat> findByParticipantsIn(List<String> participants);
    List<Chat> findAllByType(ChatType type);
    Optional<Chat> findByChatUUIDAndType(String chatUUID, ChatType type);
    Optional<Chat> findByChatUUID(String chatUUID);
    boolean existsByChatUUID(String id);
}
