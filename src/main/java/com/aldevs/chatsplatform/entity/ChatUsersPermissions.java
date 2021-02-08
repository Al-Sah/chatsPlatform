package com.aldevs.chatsplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class ChatUsersPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "chat_uuid")
    private String chatUUID;
    private String username;

    @ElementCollection(targetClass = ChatPermission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ChatPermission> participantPermissions;

    public ChatUsersPermissions(String chatUUID, String username, Set<ChatPermission> participantPermissions) {
        this.chatUUID = chatUUID;
        this.username = username;
        this.participantPermissions = participantPermissions;
    }
}
