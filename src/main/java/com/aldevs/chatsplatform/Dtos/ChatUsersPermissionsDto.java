package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.ChatPermission;
import com.aldevs.chatsplatform.entity.ChatUsersPermissions;
import lombok.Data;

import java.util.Set;

@Data
public class ChatUsersPermissionsDto {

    private String chatUUID;
    private String username;
    private Set<ChatPermission> participantPermissions;

    public ChatUsersPermissionsDto(ChatUsersPermissions permissions){
        this.chatUUID = permissions.getChatUUID();
        this.participantPermissions = permissions.getParticipantPermissions();
        this.username = permissions.getUsername();
    }
}
