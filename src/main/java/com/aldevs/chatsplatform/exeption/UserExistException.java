package com.aldevs.chatsplatform.exeption;

public class UserExistException extends RuntimeException{

    public UserExistException(String username) {
        super("User with name [ " + username + " ] exists!");
    }
}
