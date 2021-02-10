package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChatDto {

    private List<String> participants;
    private String chatUUID;
    private ChatType type;
    private Date creationDate;
    private String description;

    public ChatDto(Chat chat) {
        this.participants = chat.getParticipants();
        this.chatUUID = chat.getChatUUID();
        this.type = chat.getType();
        this.creationDate = chat.getCreationDate();
        this.description = chat.getDescription();
    }
}
