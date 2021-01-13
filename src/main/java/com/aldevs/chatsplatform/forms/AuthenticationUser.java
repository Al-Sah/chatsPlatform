package com.aldevs.chatsplatform.forms;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationUser {
    @NotBlank(message = "username cannot be empty")
    private String username;
    @NotBlank(message = "password cannot be empty")
    private String password;
}
