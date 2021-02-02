package com.aldevs.chatsplatform.exeption;

public class ChatExistException extends RuntimeException{
    public ChatExistException(String creator, String chatUUID) {
        super("This chat have already created. Creator: "+creator+". Chat_uuid:" + chatUUID);
    }
}
