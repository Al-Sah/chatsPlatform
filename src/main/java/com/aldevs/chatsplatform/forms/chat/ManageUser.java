package com.aldevs.chatsplatform.forms.chat;

import com.aldevs.chatsplatform.annotation.ParticipantsValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ManageUser {

    @ParticipantsValidator
    private String username;
    @NotBlank(message = "field chat_uuid cannot be empty")
    private String chatUUID;

}
