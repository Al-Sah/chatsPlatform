package com.aldevs.chatsplatform.forms.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatCreationResponse {

    String firstUsername;
    String secondUsername;
    String chatUUID;


}
