package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatDto;
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

@Service
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final ChatsRepository chatsRepository;
    private final ChatPermissionsRepository permissionsRepository;

    public ChatServiceImpl(UserService userService, ChatsRepository chatsRepository, ChatPermissionsRepository permissionsRepository) {
        this.userService = userService;
        this.chatsRepository = chatsRepository;
        this.permissionsRepository = permissionsRepository;
    }

    private Chat generateChat(List<String> participants, String chatUUID){
        Optional<Chat> optionalChatObject = chatsRepository.findByParticipantsIn(participants);
        if(optionalChatObject.isPresent()){
            throw new ChatExistException(optionalChatObject.get().getType().toString(), optionalChatObject.get().getChatUUID());
        }
        return new Chat(participants, chatUUID, ChatType.LOCAL_CHAT);
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
    public List<ChatDto> getPublicGroups() {
        return null;
    }

    @Override
    public ChatDto getPublicGroup(UserDetails info, String chatUUID) {
        return null;
    }

    @Override
    public ChatDto getPrivateGroup(UserDetails info, String chatUUID, String pswd) {
        return null;
    }


}
