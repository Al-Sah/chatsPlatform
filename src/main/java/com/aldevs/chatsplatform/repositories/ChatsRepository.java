package com.aldevs.chatsplatform.repositories;

import com.aldevs.chatsplatform.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatsRepository extends JpaRepository<Chat, Long> {

    //boolean existsByParticipantsIn(List<String> participants);
    Optional<Chat> findByParticipantsIn(List<String> participants);
}
