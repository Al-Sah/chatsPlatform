package com.aldevs.chatsplatform.repositories;

import com.aldevs.chatsplatform.entity.ChatUsersPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatPermissionsRepository extends JpaRepository<ChatUsersPermissions,Long> {
    Optional<ChatUsersPermissions> findByChatUUIDAndUsername(String id, String username);
}
