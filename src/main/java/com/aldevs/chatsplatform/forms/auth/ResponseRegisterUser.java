package com.aldevs.chatsplatform.forms.auth;

import com.aldevs.chatsplatform.entity.User;
import lombok.Data;

@Data
public class ResponseRegisterUser {

    private String profileName;
    private String username;
    private String email;
    private String userAbout;

    public ResponseRegisterUser(User user){
        profileName = user.getProfileName();
        username = user.getUsername();
        email = user.getEmail();
        userAbout = user.getUserAbout();
    }
}
