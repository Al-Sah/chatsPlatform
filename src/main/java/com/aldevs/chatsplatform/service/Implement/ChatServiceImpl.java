package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.exeption.ChatExistException;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import com.aldevs.chatsplatform.service.ChatService;
import com.aldevs.chatsplatform.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final ChatsRepository chatsRepository;

    public ChatServiceImpl(UserService userService, ChatsRepository chatsRepository) {
        this.userService = userService;
        this.chatsRepository = chatsRepository;
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
    public Object sendMessage(Object message) {
        return null;
    }
}
