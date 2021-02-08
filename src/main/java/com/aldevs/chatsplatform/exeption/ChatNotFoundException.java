package com.aldevs.chatsplatform.exeption;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException(String id) {
        super("ChatDto with id ["+id+"] not found");
    }
}
