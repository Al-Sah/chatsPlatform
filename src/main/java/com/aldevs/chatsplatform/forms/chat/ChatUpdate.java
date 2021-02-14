package com.aldevs.chatsplatform.forms.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatUpdate {

    @NotBlank(message = "field chat_uuid cannot be empty")
    private String chatUUID;
    private String newDescription;
    @NotNull
    private boolean changeType;
    private String password;
}
