package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatPermission;
import com.aldevs.chatsplatform.entity.ChatType;
import com.aldevs.chatsplatform.entity.ChatUsersPermissions;
import com.aldevs.chatsplatform.exeption.ChatExistException;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import com.aldevs.chatsplatform.repositories.ChatPermissionsRepository;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatsRepository chatsRepository;
    private final ChatPermissionsRepository permissionsRepository;

    public ChatServiceImpl(ChatsRepository chatsRepository, ChatPermissionsRepository permissionsRepository) {
        this.chatsRepository = chatsRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public ChatDto createLocalChat(UserDetails creator, String participant) {
        List<String> participants = new LinkedList<>(Arrays.asList(creator.getUsername(),participant));

        String chatUUID = UUID.randomUUID().toString();
        participants.forEach(p-> permissionsRepository.save(new ChatUsersPermissions(chatUUID, p, Collections.singleton(ChatPermission.CHAT_BASIC))));

        var oc = chatsRepository.findByTypeAndParticipantsIn(ChatType.LOCAL_CHAT, participants);
        if(oc.isPresent() && oc.get().getParticipants().containsAll(participants)){
            throw new ChatExistException("LOCAL_CHAT", oc.get().getChatUUID());
        }
        Chat chat = new Chat(participants, chatUUID, ChatType.LOCAL_CHAT, new Date(), "", "");
        chatsRepository.save(chat);
        return new ChatDto(chat);
    }

    @Override
    public List<ChatDto> getPublicGroups() {
        return chatsRepository.findAllByType(ChatType.PUBLIC_GROUP).stream().map(ChatDto::new).collect(Collectors.toList());
    }

    @Override
    public ChatDto joinPublicGroup(UserDetails info, String chatUUID) {
        // TODO Is group part
        Chat group = chatsRepository.findByChatUUIDAndType(chatUUID, ChatType.PUBLIC_GROUP).orElseThrow(()-> new ChatNotFoundException(chatUUID));
        group.getParticipants().add(info.getUsername());
        chatsRepository.save(group);
        permissionsRepository.save(new ChatUsersPermissions(chatUUID, info.getUsername(), Collections.singleton(ChatPermission.CHAT_BASIC)));
        return new ChatDto(group);
    }

    @Override
    public ChatDto joinPrivateGroup(UserDetails info, String chatUUID, String pswd) {
        Chat group = chatsRepository.findByChatUUIDAndType(chatUUID, ChatType.PRIVATE_GROUP).orElseThrow(()-> new ChatNotFoundException(chatUUID));
        if(group.getPassword().equals(pswd)){
            group.getParticipants().add(info.getUsername());
        } else throw new RuntimeException("Wrong password");
        chatsRepository.save(group);
        permissionsRepository.save(new ChatUsersPermissions(chatUUID, info.getUsername(), Collections.singleton(ChatPermission.CHAT_BASIC)));
        return new ChatDto(group);
    }

    @Override
    public ChatDto createGroup(UserDetails info, GroupCreationForm form) {
        String chatUUID = UUID.randomUUID().toString();
        //TODO logic !!!
        var chat = new Chat(form.getParticipants(), chatUUID, ChatType.PUBLIC_GROUP, new Date(), form.getDescription(), form.getIsPrivate() ? form.getPassword() : "");
        permissionsRepository.save(new ChatUsersPermissions(chatUUID, info.getUsername(), Collections.singleton(ChatPermission.GROUP_CHAT_CREATOR)));
        form.getParticipants().add(info.getUsername());
        form.getParticipants().forEach(p-> permissionsRepository.save(new ChatUsersPermissions(chatUUID, p, Collections.singleton(ChatPermission.CHAT_BASIC))));
        chatsRepository.save(chat);
        return new ChatDto(chat);
    }

    @Override
    public ChatDto viewChat(UserDetails info, String chatUUID) {
        //TODO logic
        return new ChatDto(chatsRepository.findByChatUUID(chatUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID)));
    }


}
