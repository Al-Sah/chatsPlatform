package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.entity.*;
import com.aldevs.chatsplatform.exeption.ChatExistException;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.*;
import com.aldevs.chatsplatform.repositories.*;
import com.aldevs.chatsplatform.service.*;
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
    private final ChatPermissionsRepository permissionsRepository;

    public ChatServiceImpl(UserService userService, ChatsRepository chatsRepository, ChatMessagesRepository messagesRepository, ChatPermissionsRepository permissionsRepository) {
        this.userService = userService;
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
        this.permissionsRepository = permissionsRepository;
    }

    private Chat generateChat(List<String> participants, String chatUUID){
        Optional<Chat> optionalChatObject = chatsRepository.findByParticipantsIn(participants);
        if(optionalChatObject.isPresent()){
            throw new ChatExistException(optionalChatObject.get().getType().toString(), optionalChatObject.get().getChatUUID());
        }
        return new Chat(participants, chatUUID, ChatType.LOCAL_CHAT);
    }

    private ChatTextMessage getMessage(String chatUUID, String messageUUID){
        return messagesRepository.findByChatUUIDAndMessageUUID(chatUUID, messageUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID));
    }

    private void generatePermissionsForLocalChat(String id, String creator, String participant){
        // TODO configuration ?
        permissionsRepository.save(new ChatUsersPermissions(id, creator,     Collections.singleton(ChatPermission.CHAT_BASIC)));
        permissionsRepository.save(new ChatUsersPermissions(id, participant, Collections.singleton(ChatPermission.CHAT_BASIC)));
    }

    @Override
    public ChatCreationResponse createChat(UserDetails creator, String participant) {
        if(!userService.existByUsername(participant)){
            throw new UsernameNotFoundException("Username ["+participant+"] not found");
        }
        List<String> participants = new LinkedList<>(Arrays.asList(creator.getUsername(),participant));

        String chatUUID = UUID.randomUUID().toString();
        generatePermissionsForLocalChat(chatUUID, creator.getUsername(), participant);
        chatsRepository.save(generateChat(participants, chatUUID));
        return new ChatCreationResponse(creator.getUsername(), participant, chatUUID);
    }

    @Override
    public ChatTextMessageDto sendMessage(UserDetails author, ChatTextMessageRequest message) {
        ChatTextMessage chatTextMessage = new ChatTextMessage(
                message.getChatUUID(),
                UUID.randomUUID().toString(),
                message.getContent(),
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
