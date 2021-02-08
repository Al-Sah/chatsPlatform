package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ChatService {

    ChatCreationResponse createChat(UserDetails creator, String participant);
    List<ChatDto> getPublicGroups();
    ChatDto getPublicGroup(UserDetails info, String chatUUID);
    ChatDto getPrivateGroup(UserDetails info, String chatUUID, String pswd);
}
