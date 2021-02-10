package com.aldevs.chatsplatform.forms.chat;

import com.aldevs.chatsplatform.annotation.ParticipantsValidator;
import lombok.Data;

import java.util.List;

@Data
public class GroupCreationForm {

    private String description;
    private Boolean isPrivate;
    private String password;
    @ParticipantsValidator
    private List<String> participants;
}
