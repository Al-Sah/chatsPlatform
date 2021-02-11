package com.aldevs.chatsplatform.exeption;

public class DictionaryWordNotFound extends RuntimeException{
    public DictionaryWordNotFound(String word) {
        super("Word ["+word+"] not found");
    }
}
