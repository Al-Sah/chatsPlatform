package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.entity.ChatTextMessage;
import com.aldevs.chatsplatform.entity.MessageState;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.service.DictionaryService;
import com.aldevs.chatsplatform.service.MessageService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChatMessagesRepository messagesRepository;
    private final DictionaryService dictionaryService;

    public MessageServiceImpl(ChatMessagesRepository messagesRepository, DictionaryService dictionaryService) {
        this.messagesRepository = messagesRepository;
        this.dictionaryService = dictionaryService;
    }

    private ChatTextMessage getMessage(String chatUUID, String messageUUID){
        return messagesRepository.findByChatUUIDAndMessageUUID(chatUUID, messageUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID));
    }

    @Override
    public ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message) {
        ChatTextMessage chatTextMessage = new ChatTextMessage(
                message.getChatUUID(),
                UUID.randomUUID().toString(),
                dictionaryService.validateContent(message.getContent()),
                message.getContent(),
                author.getUsername(),
                new Date(),
                MessageState.DELIVERED);
        messagesRepository.save(chatTextMessage);
        return new ChatTextMessageDto(chatTextMessage);
    }

    @Override
    public ChatTextMessageDto editMessage(EditTextChatMessage message) {
        ChatTextMessage textMessage = getMessage(message.getChatUUID(), message.getMessageUUID());
        textMessage.setOriginalContent(message.getNewContent());
        textMessage.setPublicContent(message.getNewContent()); // TODO content check
        messagesRepository.save(textMessage);
        return new ChatTextMessageDto(textMessage);
    }

    @Override
    public ChatTextMessageDto deleteMessage(DeleteTextMessage message) {
        ChatTextMessage textMessage = getMessage(message.getChatUUID(), message.getMessageUUID());
        textMessage.setPublicContent("DELETED");
        textMessage.setState(MessageState.DELETED);
        messagesRepository.save(textMessage);
        return new ChatTextMessageDto(textMessage);
    }

    @Override
    public List<ChatTextMessageDto> getLastMessages(String chatUUID, String number) {
        List<ChatTextMessageDto> messages; // TODO Page ?
        return null;
    }

    @Override
    public List<ChatTextMessageDto> getAllMessages(String chatUUID) {
        return messagesRepository.findAllByChatUUID(chatUUID).stream().map(ChatTextMessageDto::new).collect(Collectors.toList());
    }
}
