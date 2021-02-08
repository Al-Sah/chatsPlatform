package com.aldevs.chatsplatform.service;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MessageService {

    ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message);
    ChatTextMessageDto editMessage(EditTextChatMessage message);
    ChatTextMessageDto deleteMessage(DeleteTextMessage message);
    List<ChatTextMessageDto> getLastMessages(String chat, String number);
    List<ChatTextMessageDto> getAllMessages(String chat);
}
