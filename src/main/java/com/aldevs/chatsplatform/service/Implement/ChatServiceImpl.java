package com.aldevs.chatsplatform.service.Implement;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.Dtos.ChatUsersPermissionsDto;
import com.aldevs.chatsplatform.entity.Chat;
import com.aldevs.chatsplatform.entity.ChatPermission;
import com.aldevs.chatsplatform.entity.ChatType;
import com.aldevs.chatsplatform.entity.ChatUsersPermissions;
import com.aldevs.chatsplatform.exeption.ChatExistException;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.ChatUpdate;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import com.aldevs.chatsplatform.forms.chat.ManageUser;
import com.aldevs.chatsplatform.forms.chat.SetPermissionsForm;
import com.aldevs.chatsplatform.repositories.ChatPermissionsRepository;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ChatDto leaveGroup(UserDetails info, String chatUUID) {
        permissionsRepository.deleteAllByChatUUIDAndUsername(chatUUID, info.getUsername());
        Chat group = chatsRepository.findByChatUUID(chatUUID).orElseThrow();
        group.getParticipants().remove(info.getUsername());
        return new ChatDto(group);
    }

    @Override
    public List<ChatDto> getMineGroups(UserDetails info) {
        return chatsRepository.findAllByParticipantsIn(Collections.singletonList(info.getUsername())).stream().map(ChatDto::new).collect(Collectors.toList());
    }

    @Override
    public ChatDto createGroup(UserDetails info, GroupCreationForm form) {
        String currentUser = info.getUsername();
        String chatUUID = UUID.randomUUID().toString();

        List<String> participants = form.getParticipants();
        participants.add(currentUser);

        var chat = new Chat(
                participants,
                chatUUID,
                form.getIsPrivate() ? ChatType.PRIVATE_GROUP : ChatType.PUBLIC_GROUP,
                new Date(),
                form.getDescription(),
                form.getIsPrivate() ? form.getPassword() : "");
        chatsRepository.save(chat);

        permissionsRepository.save(new ChatUsersPermissions(chatUUID, currentUser, Collections.singleton(ChatPermission.GROUP_CHAT_CREATOR)));
        for (String participant : participants) {
            if(!participant.equals(currentUser)){
                permissionsRepository.save(new ChatUsersPermissions(chatUUID, participant, Collections.singleton(ChatPermission.CHAT_BASIC)));
            }
        }
        return new ChatDto(chat);
    }

    @Override
    public ChatDto viewChat(String chatUUID) {
        return new ChatDto(chatsRepository.findByChatUUID(chatUUID).orElseThrow());
    }

    @Override
    public ChatDto updateChat(ChatUpdate form) {
        Chat chat = chatsRepository.findByChatUUID(form.getChatUUID()).orElseThrow(()-> new ChatNotFoundException(form.getChatUUID()));
        if(chat.getType().equals(ChatType.LOCAL_CHAT)){
            throw new RuntimeException("You cannot join user to local chat");
        } else if(form.isChangeType()){
            if(chat.getType().equals(ChatType.PRIVATE_GROUP)){ //TODO permission check
                chat.setType(ChatType.PUBLIC_GROUP);
            } else{
                chat.setType(ChatType.PRIVATE_GROUP);
                chat.setPassword(form.getPassword() != null ? form.getPassword() : chat.getPassword());
            }
        }
        if(form.getNewDescription() != null && !form.getNewDescription().isEmpty()){
            chat.setDescription(form.getNewDescription());
        }
        chatsRepository.save(chat);
        return new ChatDto(chat);
    }

    @Override
    @Transactional
    public void deleteChat(String chatUUID) {
        permissionsRepository.deleteAllByChatUUID(chatUUID);
        chatsRepository.deleteByChatUUID(chatUUID);
    }


    @Override
    public ChatUsersPermissionsDto setPermissions(SetPermissionsForm form) {
        ChatUsersPermissions currentPermissions = permissionsRepository
                .findByChatUUIDAndUsername(form.getChatUUID(), form.getUsername()).orElseThrow(); // TODO

        Set<ChatPermission> newPermissions = new HashSet<>();

        StringBuilder stringBuilder = new StringBuilder();
        String parsingResult;
        for (String permission : form.getPermissions()) {
            try {
                newPermissions.add(ChatPermission.valueOf(permission));
            } catch (IllegalArgumentException e) {
                stringBuilder.append(e.getMessage());
            }
        }if(newPermissions.contains(ChatPermission.GROUP_CHAT_CREATOR)){
            throw new RuntimeException("You cannot set 'creator' permission");
        }
        parsingResult = stringBuilder.toString();
        if(!parsingResult.isEmpty()){
            throw new RuntimeException(parsingResult); // TODO normal Exception
        }
        currentPermissions.setParticipantPermissions(newPermissions);
        permissionsRepository.save(currentPermissions);
        return new ChatUsersPermissionsDto(currentPermissions);
    }



    @Override
    public ChatDto addUser(ManageUser form) {
        Chat group = chatsRepository.findByChatUUID(form.getChatUUID()).orElseThrow(()-> new ChatNotFoundException(form.getChatUUID()));
        if(group.getType().equals(ChatType.LOCAL_CHAT)){
            throw new RuntimeException("You cannot join user to local chat");
        } else if(group.getParticipants().contains(form.getUsername())){
            throw new RuntimeException("User is a participant");
        }
        chatsRepository.save(group);
        permissionsRepository.save(new ChatUsersPermissions(form.getChatUUID(), form.getUsername(), Collections.singleton(ChatPermission.CHAT_BASIC)));
        return new ChatDto(group);
    }

    @Override
    @Transactional
    public ChatDto deleteUser(ManageUser form) {
        Chat group = chatsRepository.findByChatUUID(form.getChatUUID()).orElseThrow(()-> new ChatNotFoundException(form.getChatUUID()));
        if(group.getType().equals(ChatType.LOCAL_CHAT)){
            throw new RuntimeException("You cannot delete user from local chat");
        }else if(! group.getParticipants().contains(form.getUsername())){
            throw new RuntimeException("User is not a participant");
        }
        group.getParticipants().remove(form.getUsername());
        chatsRepository.save(group);
        permissionsRepository.deleteAllByChatUUIDAndUsername(form.getChatUUID(), form.getUsername());
        return new ChatDto(group);
    }
}
