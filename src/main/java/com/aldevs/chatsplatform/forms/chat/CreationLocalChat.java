package com.aldevs.chatsplatform.forms.chat;

import com.aldevs.chatsplatform.annotation.ParticipantsValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreationLocalChat {
    @NotBlank
    @ParticipantsValidator
    private String participant;
}
