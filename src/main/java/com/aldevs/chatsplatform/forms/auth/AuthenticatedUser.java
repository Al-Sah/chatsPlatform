package com.aldevs.chatsplatform.forms.auth;

import lombok.Data;

@Data
public class AuthenticatedUser {

    private String username;
    private String jwtToken;

    public AuthenticatedUser(String username, String token) {
        this.username = username;
        this.jwtToken = token;
    }
}
