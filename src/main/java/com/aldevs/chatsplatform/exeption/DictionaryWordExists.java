package com.aldevs.chatsplatform.exeption;

public class DictionaryWordExists extends RuntimeException{

    public DictionaryWordExists(String word) {
        super("Word ["+word+"] exists");
    }
}
