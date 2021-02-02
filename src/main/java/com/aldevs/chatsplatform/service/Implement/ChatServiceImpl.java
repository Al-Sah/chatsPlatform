package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatTextMessage;
import com.aldevs.chatsplatform.exeption.ChatExistException;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.exeption.NotChatParticipantException;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import com.aldevs.chatsplatform.service.ChatService;
import com.aldevs.chatsplatform.service.EditTextChatMessage;
import com.aldevs.chatsplatform.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final ChatsRepository chatsRepository;
    private final ChatMessagesRepository messagesRepository;

    public ChatServiceImpl(UserService userService, ChatsRepository chatsRepository, ChatMessagesRepository messagesRepository) {
        this.userService = userService;
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
    }

    private void isChatParticipant(String username, String chatUUID){
        if(chatsRepository.findByChatUUID(chatUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID))
                .getParticipants().stream().noneMatch(element -> element.equals(username))){
            throw new NotChatParticipantException();
        }
    }

    private Chat generateChat(List<String> participants, String chatUUID, String creator){
        Optional<Chat> optionalChatObject = chatsRepository.findByParticipantsIn(participants);
        if(optionalChatObject.isPresent()){
            throw new ChatExistException(optionalChatObject.get().getCreator(), optionalChatObject.get().getChatUUID());
        }
        return new Chat(participants, chatUUID, creator);
    }

    @Override
    public ChatCreationResponse createChat(UserDetails creator, String participant) {
        if(!userService.existByUsername(participant)){
            throw new UsernameNotFoundException("Username ["+participant+"] not found");
        }
        List<String> participants = new LinkedList<>();
        participants.add(creator.getUsername());
        participants.add(participant);
        String chatUUID = UUID.randomUUID().toString();

        chatsRepository.save(generateChat(participants, chatUUID, creator.getUsername()));
        return new ChatCreationResponse(creator.getUsername(), participant, chatUUID);
    }

    @Override
    public ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message) {
        isChatParticipant(author.getUsername(), message.getChatUUID());
        ChatTextMessage chatTextMessage = new ChatTextMessage(
                message.getChatUUID(),
                message.getContent(),
                message.getContent(),
                author.getUsername(),
                new Date());

        messagesRepository.save(chatTextMessage);
        return new ChatTextMessageDto(chatTextMessage);
    }

    @Override
    public ChatTextMessageDto editMessage(UserDetails author, EditTextChatMessage message) {
        isChatParticipant(author.getUsername(), message.getChatUUID());
        ChatTextMessage textMessage = messagesRepository.findByChatUUIDAndMessageUUID(message.getChatUUID(), message.getMessageUUID())
                .orElseThrow(()-> new ChatNotFoundException(message.getChatUUID()));
        if(!author.getUsername().equals(textMessage.getAuthor())){
            throw new RuntimeException("You cannot edit message");
        }
        textMessage.setOriginalContent(message.getNewContent());
        textMessage.setPublicContent(message.getNewContent());
        messagesRepository.save(textMessage);
        return new ChatTextMessageDto(textMessage);
    }

    @Override
    public List<ChatTextMessageDto> getLastMessages(UserDetails userDetails, String chatUUID, String number) {
        isChatParticipant(userDetails.getUsername(), chatUUID);
        List<ChatTextMessageDto> messages; // TODO Page ?
        return null;
    }

    @Override
    public List<ChatTextMessageDto> getAllMessages(UserDetails userDetails, String chatUUID) {
        isChatParticipant(userDetails.getUsername(), chatUUID);
        return messagesRepository.findAllByChatUUID(chatUUID).stream().map(ChatTextMessageDto::new).collect(Collectors.toList());
    }
}
