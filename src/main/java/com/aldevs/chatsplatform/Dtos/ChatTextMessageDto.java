package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.ChatTextMessage;
import lombok.Data;
import java.util.Date;

@Data
public class ChatTextMessageDto {

    private String chatUUID;
    private String publicContent;
    private String originalContent;
    private String author;
    private Date timestamp;

    public ChatTextMessageDto(String chatUUID, String publicContent, String originalContent, String author, Date timestamp) {
        this.chatUUID = chatUUID;
        this.publicContent = publicContent;
        this.originalContent = originalContent;
        this.author = author;
        this.timestamp = timestamp;
    }
    public ChatTextMessageDto(ChatTextMessage message) {
        this.chatUUID = message.getChatUUID();
        this.publicContent = message.getPublicContent();
        this.originalContent = message.getOriginalContent();
        this.author = message.getAuthor();
        this.timestamp = message.getTimestamp();
    }
}
