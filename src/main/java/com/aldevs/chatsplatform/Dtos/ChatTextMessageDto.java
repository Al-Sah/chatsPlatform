package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.ChatTextMessage;
import com.aldevs.chatsplatform.entity.MessageState;
import lombok.Data;
import java.util.Date;

@Data
public class ChatTextMessageDto {

    private String chatUUID;
    private String messageUUID;
    private String publicContent;
    private String originalContent;
    private String author;
    private Date timestamp;
    private MessageState state;

    public ChatTextMessageDto(ChatTextMessage message) {
        this.chatUUID = message.getChatUUID();
        this.messageUUID = message.getMessageUUID();
        this.publicContent = message.getPublicContent();
        this.originalContent = message.getOriginalContent();
        this.author = message.getAuthor();
        this.timestamp = message.getTimestamp();
        this.state = message.getState();
    }
}
