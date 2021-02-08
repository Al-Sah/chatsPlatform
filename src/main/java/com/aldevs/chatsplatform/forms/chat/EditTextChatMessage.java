package com.aldevs.chatsplatform.forms.chat;

import lombok.Data;

@Data
public class EditTextChatMessage {

    private String messageUUID;
    private String chatUUID;
    private String newContent;
}
