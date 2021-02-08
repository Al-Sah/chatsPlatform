package com.aldevs.chatsplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "chat_messages")
@NoArgsConstructor
public class ChatTextMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "chat_uuid")
    private String chatUUID;
    @Column(name = "message_uuid")
    private String messageUUID;
    @Column(name = "public_content")
    private String publicContent;
    @Column(name = "original_content")
    private String originalContent;
    private String author;
    private Date timestamp;
    private MessageState state;

    public ChatTextMessage(String chatUUID, String messageUUID, String publicContent, String originalContent, String author, Date timestamp, MessageState state) {
        this.chatUUID = chatUUID;
        this.messageUUID = messageUUID;
        this.publicContent = publicContent;
        this.originalContent = originalContent;
        this.author = author;
        this.timestamp = timestamp;
        this.state = state;
    }
}
