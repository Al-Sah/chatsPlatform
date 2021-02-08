package com.aldevs.chatsplatform.forms.chat;

import lombok.Data;

@Data
public class DeleteTextMessage {
    private String messageUUID;
    private String chatUUID;
}
