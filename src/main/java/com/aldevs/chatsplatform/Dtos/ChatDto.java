package com.aldevs.chatsplatform.Dtos;

import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatDto {

    private List<String> participants;
    private String chatUUID;
    private ChatType type;
    private Date creationDate;
    private String description;
    private List<ActionDTO> actions;

    public ChatDto(Chat chat) {
        this.participants = chat.getParticipants();
        this.chatUUID = chat.getChatUUID();
        this.type = chat.getType();
        this.creationDate = chat.getCreationDate();
        this.description = chat.getDescription();
        this.actions = chat.getActions().stream().map(ActionDTO::new).collect(Collectors.toList());
    }
}
