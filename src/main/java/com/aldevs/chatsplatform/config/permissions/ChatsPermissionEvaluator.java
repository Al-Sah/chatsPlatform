package com.aldevs.chatsplatform.config.permissions;

import com.aldevs.chatsplatform.entity.*;
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
import java.util.*;

@Component
public class ChatsPermissionEvaluator implements PermissionEvaluator {

    private final ChatPermissionsRepository permissionsRepository;
    private final ChatsRepository chatsRepository;
    private final ChatMessagesRepository messagesRepository;

    public ChatsPermissionEvaluator(ChatPermissionsRepository permissionsRepository, ChatsRepository chatsRepository, ChatMessagesRepository messagesRepository) {
        this.permissionsRepository = permissionsRepository;
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
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

    private boolean hasPermissionMessageAction(Authentication authentication, Object target, PermissionRegistry.Action permission){
        String chatUUID = getChatUUID(target), currentUsername = authentication.getName();
        if(!chatsRepository.existsByChatUUID(chatUUID)) throw new ChatNotFoundException(chatUUID);

        // TODO admin and Moderator configuration
        if(authentication.getAuthorities().contains(Role.ROLE_ADMIN) || authentication.getAuthorities().contains(Role.ROLE_MODERATOR)) return true;

        Set<ChatPermission> userPermission = permissionsRepository.findByChatUUIDAndUsername(chatUUID,currentUsername)
                .orElseThrow(()->new RuntimeException("User dont have any permission in chat")).getParticipantPermissions();

        if(permission.equals(PermissionRegistry.Action.CREATE)) {
            return (userPermission.contains(ChatPermission.SEND_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if( permission.equals(PermissionRegistry.Action.READ)){
            return (userPermission.contains(ChatPermission.READ_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if(permission.equals(PermissionRegistry.Action.UPDATE)){
            if(currentUsername.equals(getMessageAuthor(chatUUID, ((EditTextChatMessage)target).getMessageUUID()))){
                return (userPermission.contains(ChatPermission.EDIT_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }
            else return (userPermission.contains(ChatPermission.EDIT_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        else if(permission.equals(PermissionRegistry.Action.DELETE)){
            if(currentUsername.equals(getMessageAuthor(chatUUID, ((DeleteTextMessage)target).getMessageUUID()))){
                return (userPermission.contains(ChatPermission.DELETE_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }
            else return (userPermission.contains(ChatPermission.DELETE_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        return false;
    }

    private boolean hasPermissionChatAction(Authentication authentication, Object target, PermissionRegistry.Action permission){

        String chatUUID = (String)target, currentUsername = authentication.getName();
        Chat chat = chatsRepository.findByChatUUID(chatUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID));

        // TODO admin and Moderator configuration
        if(authentication.getAuthorities().contains(Role.ROLE_ADMIN) || authentication.getAuthorities().contains(Role.ROLE_MODERATOR)) return true;

        Optional<ChatUsersPermissions> part = permissionsRepository.findByChatUUIDAndUsername(chatUUID,currentUsername);
        if(permission.equals(PermissionRegistry.Action.JOIN)) {
            if(part.isPresent()){
                throw new RuntimeException("You are participant"); // TODO
            } else return true;
        } else if(permission.equals(PermissionRegistry.Action.READ)) {
            if(chat.getType().equals(ChatType.PUBLIC_GROUP)){
                return true;
            } else return part.isPresent();
        }
        Set<ChatPermission> userPermission = part.orElseThrow(()-> new RuntimeException("User dont have any permission in chat")).getParticipantPermissions();
        if(permission.equals(PermissionRegistry.Action.LEAVE)){
            if(userPermission.contains(ChatPermission.GROUP_CHAT_CREATOR)){
                throw new RuntimeException("Creator cannot leave a chat");// TODO
            } else if(chat.getType().equals(ChatType.LOCAL_CHAT)){
                throw new RuntimeException("User cannot leave a local chat");// TODO
            } else return true;
        } else if(permission.equals(PermissionRegistry.Action.DELETE)){
            if(chat.getType().equals(ChatType.LOCAL_CHAT)){
                return true;
            } else return userPermission.contains(ChatPermission.GROUP_CHAT_CREATOR); // ??
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        if(permission instanceof PermissionRegistry.PermissionPair) {
            if(((PermissionRegistry.PermissionPair) permission).getTarget().equals(PermissionRegistry.Target.MESSAGE)){
                return hasPermissionMessageAction(authentication,target, ((PermissionRegistry.PermissionPair) permission).getAction());
            }else if(((PermissionRegistry.PermissionPair) permission).getTarget().equals(PermissionRegistry.Target.CHAT)){
                return hasPermissionChatAction(authentication,target, ((PermissionRegistry.PermissionPair) permission).getAction());
            }
            else return false;
        }
        else return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
