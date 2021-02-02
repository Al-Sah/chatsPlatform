package com.aldevs.chatsplatform.service;

import lombok.Data;

@Data
public class EditTextChatMessage {

    private String messageUUID;
    private String chatUUID;
    private String newContent;
}
