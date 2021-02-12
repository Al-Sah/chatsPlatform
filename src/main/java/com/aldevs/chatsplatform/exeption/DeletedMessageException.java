package com.aldevs.chatsplatform.exeption;

public class DeletedMessageException extends RuntimeException{

    public DeletedMessageException(String message) {
        super(message);
    }
}
