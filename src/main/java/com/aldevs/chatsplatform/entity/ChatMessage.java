package com.aldevs.chatsplatform.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_uuid")
    private String chatUUID;
}
