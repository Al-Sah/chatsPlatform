package com.aldevs.chatsplatform.exeption;

public class NotChatParticipantException extends RuntimeException{

    public NotChatParticipantException() {
        super("You are not a participant of this chat");
    }
}
