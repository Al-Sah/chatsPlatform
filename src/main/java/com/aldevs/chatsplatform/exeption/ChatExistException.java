package com.aldevs.chatsplatform.exeption;

public class ChatExistException extends RuntimeException{
    public ChatExistException(String type, String chatUUID) {
        super("This chat have already created. Type: "+type+". Chat_uuid:" + chatUUID);
    }
}
