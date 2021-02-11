package com.aldevs.chatsplatform.service;

public interface ContentManager {

    String validateMessageContent(String content);
    void updateMessagesDB();
}
