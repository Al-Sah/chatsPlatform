package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.entity.ChatTextMessage;
import com.aldevs.chatsplatform.entity.MessageState;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.exeption.DeletedMessageException;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.service.ContentManager;
import com.aldevs.chatsplatform.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChatMessagesRepository messagesRepository;
    private final ContentManager contentManager;

    public MessageServiceImpl(ChatMessagesRepository messagesRepository, ContentManager contentManager) {
        this.messagesRepository = messagesRepository;
        this.contentManager = contentManager;
    }

    private ChatTextMessage getMessage(String chatUUID, String messageUUID){
        return messagesRepository.findByChatUUIDAndMessageUUID(chatUUID, messageUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID));
    }

    @Override
    public ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message) {
        ChatTextMessage chatTextMessage = new ChatTextMessage(
                message.getChatUUID(),
                UUID.randomUUID().toString(),
                contentManager.validateMessageContent(message.getContent()),
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
        if(textMessage.getState().equals(MessageState.DELETED)){
            throw new DeletedMessageException("You cannot modify 'Deleted' message ");
        }
        textMessage.setState(MessageState.EDITED);
        textMessage.setOriginalContent(message.getNewContent());
        textMessage.setPublicContent(contentManager.validateMessageContent(message.getNewContent()));
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
    public ChatTextMessageDto recoverMessage(DeleteTextMessage message) {
        ChatTextMessage textMessage = getMessage(message.getChatUUID(), message.getMessageUUID());
        if(! textMessage.getState().equals(MessageState.DELETED)){
            throw new DeletedMessageException("Nothing to recover");
        }
        textMessage.setState(MessageState.RECOVERED);
        textMessage.setPublicContent(contentManager.validateMessageContent(textMessage.getOriginalContent()));
        messagesRepository.save(textMessage);
        return new ChatTextMessageDto(textMessage);
    }

    @Override
    public List<ChatTextMessageDto> getLast20Messages(String chatUUID) {
        return messagesRepository.findFirst20ByChatUUID(chatUUID).stream().map(ChatTextMessageDto::new).collect(Collectors.toList());
    }

    @Override
    public List<ChatTextMessageDto> getMessagesPage(String chatUUID, String page) {
        return messagesRepository.findAllByChatUUID(chatUUID, PageRequest.of(Integer.parseInt(page), 100, Sort.by(Sort.Direction.DESC, "timestamp")))
               .stream().map(ChatTextMessageDto::new).collect(Collectors.toList());
    }

    @Override
    public List<ChatTextMessageDto> getAllMessages(String chatUUID) {
        return messagesRepository.findAllByChatUUID(chatUUID).stream().map(ChatTextMessageDto::new).collect(Collectors.toList());
    }
}
