package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface ChatService {

    ChatCreationResponse createChat(UserDetails creator, String participant);
    Object sendMessage(Object message);

}
