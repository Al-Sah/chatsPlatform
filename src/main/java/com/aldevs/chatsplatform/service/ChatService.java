package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.Dtos.ChatUsersPermissionsDto;
import com.aldevs.chatsplatform.forms.chat.ChatUpdate;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import com.aldevs.chatsplatform.forms.chat.ManageUser;
import com.aldevs.chatsplatform.forms.chat.SetPermissionsForm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ChatService {

    ChatDto createLocalChat(UserDetails creator, String participant);
    ChatDto createGroup(UserDetails info, GroupCreationForm form);
    ChatDto viewChat(String chatUUID);
    ChatDto updateChat(ChatUpdate form);
    void  deleteChat(String chatUUID);

    List<ChatDto> getPublicGroups();

    ChatDto joinPublicGroup(UserDetails info, String chatUUID);
    ChatDto joinPrivateGroup(UserDetails info, String chatUUID, String pswd);
    ChatDto leaveGroup(UserDetails info, String chatUUID);

    List<ChatDto> getMineGroups(UserDetails info);

    ChatUsersPermissionsDto setPermissions(SetPermissionsForm form);
    ChatDto addUser(ManageUser form);
    ChatDto deleteUser(ManageUser form);
}
