package com.aldevs.chatsplatform.config.permissions;

import com.aldevs.chatsplatform.entity.*;
import com.aldevs.chatsplatform.exeption.ChatNotFoundException;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import com.aldevs.chatsplatform.repositories.ChatMessagesRepository;
import com.aldevs.chatsplatform.repositories.ChatPermissionsRepository;
import com.aldevs.chatsplatform.repositories.ChatsRepository;
import lombok.AllArgsConstructor;
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
    private final ModeratorPermissionsConfiguration moderatorPermissions;

    public ChatsPermissionEvaluator(ChatPermissionsRepository permissionsRepository, ChatsRepository chatsRepository, ChatMessagesRepository messagesRepository, ModeratorPermissionsConfiguration moderatorPermissions) {
        this.permissionsRepository = permissionsRepository;
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
        this.moderatorPermissions = moderatorPermissions;
    }

    private final Set<ChatPermission> rolesSet = new HashSet<>(Arrays.asList(
            ChatPermission.CHAT_BASIC, ChatPermission.GROUP_CHAT_ADMIN, ChatPermission.GROUP_CHAT_CREATOR));

    private final Set<ChatPermission> sudoSet = new HashSet<>(Arrays.asList(
            ChatPermission.GROUP_CHAT_ADMIN, ChatPermission.GROUP_CHAT_CREATOR));

    private final Set<ChatPermission> setupSet = new HashSet<>(Arrays.asList(
            ChatPermission.SET_USER_AS_SIMPLE_PARTICIPANT, ChatPermission.SET_USER_AS_ADMIN));

    @AllArgsConstructor
    private static class Target{
        private final String chatUUID;
        private final String messageUUID;
    }

    private Target getTarget(Object target){
        if(target instanceof ChatTextMessageRequest) return new Target(((ChatTextMessageRequest) target).getChatUUID(), "");
        if(target instanceof EditTextChatMessage) return new Target(((EditTextChatMessage) target).getChatUUID(), ((EditTextChatMessage) target).getMessageUUID());
        if(target instanceof DeleteTextMessage) return new Target(((DeleteTextMessage) target).getChatUUID(), ((DeleteTextMessage) target).getMessageUUID());
        else return new Target((String) target, "");
    }

    private String getMessageAuthor(String chatUUID, String messageUUID){
        return messagesRepository.findByChatUUIDAndMessageUUID(chatUUID, messageUUID).orElseThrow(RuntimeException::new).getAuthor();
    }

    private boolean checkModeratorMessagePermission(PermissionRegistry.Action permission, ChatType type){
        if(permission.equals(PermissionRegistry.Action.CREATE)) {
            if(type.equals(ChatType.LOCAL_CHAT)) return (moderatorPermissions.sendMessagesToLocalChat);
            if(type.equals(ChatType.PRIVATE_GROUP)) return (moderatorPermissions.sendMessagesToPrivateGroup);
            if(type.equals(ChatType.PUBLIC_GROUP)) return (moderatorPermissions.sendMessagesToPublicGroup);
        }
        if(permission.equals(PermissionRegistry.Action.READ)){
            if(type.equals(ChatType.LOCAL_CHAT)) return (moderatorPermissions.readMessagesInLocalChat);
            if(type.equals(ChatType.PRIVATE_GROUP)) return (moderatorPermissions.readMessagesInLocalChat);
            if(type.equals(ChatType.PUBLIC_GROUP)) return (moderatorPermissions.readMessagesInPrivateGroup);
        }
        if(permission.equals(PermissionRegistry.Action.UPDATE)){
            if(type.equals(ChatType.LOCAL_CHAT)) return (moderatorPermissions.editMessagesInLocalChat);
            if(type.equals(ChatType.PRIVATE_GROUP)) return (moderatorPermissions.editMessagesInPrivateGroup);
            if(type.equals(ChatType.PUBLIC_GROUP)) return (moderatorPermissions.editMessagesInPublicGroup);
        }
        else if(permission.equals(PermissionRegistry.Action.DELETE)){
            if(type.equals(ChatType.LOCAL_CHAT)) return (moderatorPermissions.editMessagesInLocalChat);
            if(type.equals(ChatType.PRIVATE_GROUP)) return (moderatorPermissions.editMessagesInPrivateGroup);
            if(type.equals(ChatType.PUBLIC_GROUP)) return (moderatorPermissions.editMessagesInPublicGroup);
        }
        return false;
    }
    private boolean checkModeratorChatPermission(PermissionRegistry.Action permission, ChatType type){
        if(permission.equals(PermissionRegistry.Action.CREATE)) {
            if(type.equals(ChatType.LOCAL_CHAT)) return (moderatorPermissions.deleteLocalChat);
            if(type.equals(ChatType.PRIVATE_GROUP)) return (moderatorPermissions.deletePrivateGroup);
            if(type.equals(ChatType.PUBLIC_GROUP)) return (moderatorPermissions.deletePublicGroup);
        }
        return false;
    }

    private boolean hasPermissionMessageAction(Authentication authentication, Object o, PermissionRegistry.Action permission){
        Target target = getTarget(o);
        String currentUsername = authentication.getName();
        Chat chat = chatsRepository.findByChatUUID(target.chatUUID).orElseThrow(()-> new ChatNotFoundException(target.chatUUID));

        Optional<ChatUsersPermissions> chatUsersPermissions = permissionsRepository.findByChatUUIDAndUsername(target.chatUUID,currentUsername);
        if(chatUsersPermissions.isEmpty()){
            if(authentication.getAuthorities().contains(Role.ROLE_MODERATOR)){
                return checkModeratorMessagePermission(permission, chat.getType());
            }
        }
        Set<ChatPermission> userPermission = chatUsersPermissions.orElseThrow(()->new RuntimeException("User dont have any permission in chat")).getParticipantPermissions();

        if(permission.equals(PermissionRegistry.Action.CREATE)) {
            return (userPermission.contains(ChatPermission.SEND_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        } else if( permission.equals(PermissionRegistry.Action.READ)){
            return (userPermission.contains(ChatPermission.READ_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if(permission.equals(PermissionRegistry.Action.UPDATE)){
            if(currentUsername.equals(getMessageAuthor(target.chatUUID, target.messageUUID))){
                return (userPermission.contains(ChatPermission.EDIT_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }
            else return (userPermission.contains(ChatPermission.EDIT_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        else if(permission.equals(PermissionRegistry.Action.DELETE)){
            if(currentUsername.equals(getMessageAuthor(target.chatUUID, target.messageUUID))){
                return (userPermission.contains(ChatPermission.DELETE_MESSAGES)) || !Collections.disjoint(userPermission, rolesSet);
            }
            else return (userPermission.contains(ChatPermission.DELETE_NOT_MINE_MESSAGES) || !Collections.disjoint(userPermission, sudoSet));
        }
        return false;
    }

    private boolean hasPermissionChatAction(Authentication authentication, Object target, PermissionRegistry.Action permission){
        String chatUUID = (String)target;
        String currentUsername = authentication.getName();
        Chat chat = chatsRepository.findByChatUUID(chatUUID).orElseThrow(()-> new ChatNotFoundException(chatUUID));

        Optional<ChatUsersPermissions> part = permissionsRepository.findByChatUUIDAndUsername(chatUUID,currentUsername);


        if(permission.equals(PermissionRegistry.Action.JOIN)) {
            if(chat.getParticipants().contains(currentUsername)){
                throw new RuntimeException("You are participant"); // TODO
            } else return true;
        } else if(permission.equals(PermissionRegistry.Action.READ) && chat.getType().equals(ChatType.PUBLIC_GROUP)) return true;


        if(part.isEmpty()){
            if(authentication.getAuthorities().contains(Role.ROLE_MODERATOR)){
                return checkModeratorChatPermission(permission, chat.getType());
            }
        }


        Set<ChatPermission> userPermission = part.orElseThrow(()-> new RuntimeException("You dont have any permission in chat")).getParticipantPermissions();
        if(permission.equals(PermissionRegistry.Action.READ) && !chat.getType().equals(ChatType.PUBLIC_GROUP)) {
            return (userPermission.contains(ChatPermission.READ_MESSAGES) || !Collections.disjoint(userPermission, rolesSet));
        }
        else if(permission.equals(PermissionRegistry.Action.LEAVE)){
            if(userPermission.contains(ChatPermission.GROUP_CHAT_CREATOR)){
                throw new RuntimeException("Creator cannot leave a chat");// TODO
            } else if(chat.getType().equals(ChatType.LOCAL_CHAT)){
                throw new RuntimeException("User cannot leave a local chat");// TODO
            } else return true;
        }
        else if(permission.equals(PermissionRegistry.Action.DELETE)){
            if(chat.getType().equals(ChatType.LOCAL_CHAT)){
                return true;
            } else return userPermission.contains(ChatPermission.GROUP_CHAT_CREATOR);
        }else if(permission.equals(PermissionRegistry.Action.SETUP)){
            return (userPermission.contains(ChatPermission.GROUP_CHAT_CREATOR) || !Collections.disjoint(userPermission, setupSet));
        }
        else if(permission.equals(PermissionRegistry.Action.U_ADD)){
            return (userPermission.contains(ChatPermission.ADD_NEW_USER) || !Collections.disjoint(userPermission, sudoSet));
        }
        else if(permission.equals(PermissionRegistry.Action.U_DELL)){
            return (userPermission.contains(ChatPermission.DELETE_USER) || !Collections.disjoint(userPermission, sudoSet));
        }
        else if(permission.equals(PermissionRegistry.Action.UPDATE)){
            return !Collections.disjoint(userPermission, sudoSet);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {
        if(permission instanceof PermissionRegistry.PermissionPair) {
            if(authentication.getAuthorities().contains(Role.ROLE_ADMIN)){
                return true;
            }else if(((PermissionRegistry.PermissionPair) permission).getTarget().equals(PermissionRegistry.Target.MESSAGE)){
                return hasPermissionMessageAction(authentication,target, ((PermissionRegistry.PermissionPair) permission).getAction());
            }else if(((PermissionRegistry.PermissionPair) permission).getTarget().equals(PermissionRegistry.Target.CHAT)){
                return hasPermissionChatAction(authentication,target, ((PermissionRegistry.PermissionPair) permission).getAction());
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
