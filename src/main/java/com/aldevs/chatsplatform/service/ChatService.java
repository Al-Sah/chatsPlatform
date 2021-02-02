package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ChatService {

    ChatCreationResponse createChat(UserDetails creator, String participant);
    ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message);
    ChatTextMessageDto editMessage(UserDetails author, EditTextChatMessage message);
    List<ChatTextMessageDto> getLastMessages(UserDetails userDetails, String chat, String number);
    List<ChatTextMessageDto> getAllMessages(UserDetails userDetails, String chat);
}
