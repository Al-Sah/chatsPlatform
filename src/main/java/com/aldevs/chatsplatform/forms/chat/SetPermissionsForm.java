package com.aldevs.chatsplatform.forms.chat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SetPermissionsForm {

    @NotBlank(message = "Field 'username' cannot be empty")
    String username;
    @NotBlank(message = "Field 'chatUUID' cannot be empty")
    String chatUUID;
    @NotNull
    List<String> permissions;
}
