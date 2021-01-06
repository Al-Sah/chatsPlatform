package com.aldevs.chatsplatform.forms;

import com.aldevs.chatsplatform.entity.User;
import lombok.Data;

@Data
public class ResponseUser {

    private String profileName;
    private String username;
    private String email;
    private String userAbout;

    public ResponseUser(User user){
        profileName = user.getProfileName();
        username = user.getUsername();
        email = user.getEmail();
        userAbout = user.getUserAbout();
    }
}
