package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ChatService {

    ChatDto createLocalChat(UserDetails creator, String participant);
    List<ChatDto> getPublicGroups();
    ChatDto joinPublicGroup(UserDetails info, String chatUUID);
    ChatDto joinPrivateGroup(UserDetails info, String chatUUID, String pswd);
    ChatDto createGroup(UserDetails info, GroupCreationForm form);
    ChatDto viewChat(UserDetails info, String chatUUID);
}
