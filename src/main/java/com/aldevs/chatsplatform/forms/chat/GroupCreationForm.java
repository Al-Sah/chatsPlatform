package com.aldevs.chatsplatform.forms.chat;

import com.aldevs.chatsplatform.annotation.ParticipantsValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GroupCreationForm {

    @NotNull
    private String description;
    @NotBlank(message = "field isPrivate cannot fe null")
    private Boolean isPrivate;
    private String password;
    @ParticipantsValidator
    private List<String> participants;
}
