package com.aldevs.chatsplatform.config.permissions;

import com.aldevs.chatsplatform.entity.ChatPermission;
import com.aldevs.chatsplatform.entity.Role;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.repositories.ChatPermissionsRepository;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatsPermissionEvaluator implements PermissionEvaluator {

    private final ChatPermissionsRepository permissionsRepository;
    private final ChatsRepository chatsRepository;
    private final ChatMessagesRepository messagesRepository;
    private final PermissionRegistry.MessagePermissionRegistry mpr;
    private final PermissionRegistry.ChatPermissionRegistry cpr;

    public ChatsPermissionEvaluator(
            ChatPermissionsRepository permissionsRepository,
            ChatsRepository chatsRepository, ChatMessagesRepository messagesRepository,
            PermissionRegistry.MessagePermissionRegistry mpr,
            PermissionRegistry.ChatPermissionRegistry cpr) {
        this.permissionsRepository = permissionsRepository;
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
        this.mpr = mpr;
        this.cpr = cpr;
    }

    private final Set<ChatPermission> rolesSet = new HashSet<>(Arrays.asList(
            ChatPermission.CHAT_BASIC, ChatPermission.GROUP_CHAT_ADMIN, ChatPermission.GROUP_CHAT_CREATOR));

    private final Set<ChatPermission> sudoSet = new HashSet<>(Arrays.asList(
            ChatPermission.GROUP_CHAT_ADMIN, ChatPermission.GROUP_CHAT_CREATOR));

    private String getChatUUID(Object target){
        if(target instanceof ChatTextMessageRequest) return ((ChatTextMessageRequest) target).getChatUUID();
        if(target instanceof EditTextChatMessage) return  ((EditTextChatMessage)target).getChatUUID();
        if(target instanceof DeleteTextMessage) return ((DeleteTextMessage)target).getChatUUID();
        else return (String) target;
    }

    private String getMessageAuthor(String chatUUID, String messageUUID){
        return messagesRepository.findByChatUUIDAndMessageUUID(chatUUID, messageUUID).orElseThrow(RuntimeException::new).getAuthor();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        if(!(permission instanceof PermissionRegistry.MessagePermissionRegistry)) return false;

        String chatUUID = getChatUUID(target), currentUsername = authentication.getName();
        if(!chatsRepository.existsByChatUUID(chatUUID)) throw new ChatNotFoundException(chatUUID);

        // TODO admin and Moderator configuration
        if(authentication.getAuthorities().contains(Role.ROLE_ADMIN) || authentication.getAuthorities().contains(Role.ROLE_MODERATOR)) return true;

        Set<ChatPermission> userPermission = permissionsRepository.findByChatUUIDAndUsername(chatUUID,currentUsername)
                .orElseThrow(()->new RuntimeException("User dont have any permission in chat")).getParticipantPermissions();

        if(permission.equals(mpr.create())) {
            return (userPermission.contains(ChatPermission.SEND_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if( permission.equals(mpr.read())){
            return (userPermission.contains(ChatPermission.READ_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if(permission.equals(mpr.update())){
            if(currentUsername.equals(getMessageAuthor(chatUUID, ((EditTextChatMessage)target).getMessageUUID()))){
                return (userPermission.contains(ChatPermission.EDIT_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }else return (userPermission.contains(ChatPermission.EDIT_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        else if(permission.equals(mpr.delete())){
            if(currentUsername.equals(getMessageAuthor(chatUUID, ((DeleteTextMessage)target).getMessageUUID()))){
                return (userPermission.contains(ChatPermission.DELETE_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }else return (userPermission.contains(ChatPermission.DELETE_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
