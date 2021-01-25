package com.aldevs.chatsplatform.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationUser {

    @NotBlank(message = "profileName cannot be empty")
    private String profileName;
    @NotBlank(message = "username cannot be empty")
    private String username;
    @Email
    @NotBlank(message = "email address cannot be empty")
    private String email;
    private String userAbout;
    @NotBlank(message = "password cannot be empty")
    private String password;
}
