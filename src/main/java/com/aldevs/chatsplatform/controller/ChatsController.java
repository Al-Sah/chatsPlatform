package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.Dtos.ChatUsersPermissionsDto;
import com.aldevs.chatsplatform.forms.chat.CreationLocalChat;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import com.aldevs.chatsplatform.forms.chat.SetPermissionsForm;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/app/chats")
public class ChatsController {

    private final ChatService chatService;

    public ChatsController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/local")
    public ChatDto createLocalChat(@AuthenticationPrincipal UserDetails info, @Valid @RequestBody CreationLocalChat participant){
        return chatService.createLocalChat(info, participant.getParticipant());
    }

    @PostMapping("/group")
    public ChatDto createGroup(@AuthenticationPrincipal UserDetails info, @Valid @RequestBody GroupCreationForm form){
        return chatService.createGroup(info, form);
    }

    @GetMapping("/{chat}")
    @PreAuthorize("hasPermission(#chatUUID, @cpr.read())")
    public ChatDto viewChat(@PathVariable("chat") String chatUUID){
        return chatService.viewChat(chatUUID);
    }

    @GetMapping("/list")
    public List<ChatDto> getPublicGroups(){
        return chatService.getPublicGroups();
    }
    @GetMapping("/mine")
    public List<ChatDto> getMineGroups(@AuthenticationPrincipal UserDetails info){
        return chatService.getMineGroups(info);
    }

    @PostMapping("/join/public")
    @PreAuthorize("hasPermission(#chatUUID, @cpr.join())")
    public ChatDto joinPublicGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID){
        return chatService.joinPublicGroup(info, chatUUID);
    }

    @PreAuthorize("hasPermission(#chatUUID, @cpr.join())")
    @PostMapping("/join/private")
    public ChatDto joinPrivateGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID, @RequestParam("pswd") String pswd){
        return chatService.joinPrivateGroup(info, chatUUID, pswd);
    }

    @PutMapping("/leave")
    @PreAuthorize("hasPermission(#chatUUID, @cpr.leave())")
    public ChatDto leaveGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID){
        return chatService.leaveGroup(info, chatUUID);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasPermission(#chatUUID, @cpr.delete())")
    public void deleteChat(@RequestParam("chat") String chatUUID){
        chatService.deleteChat(chatUUID);
    }

    @PutMapping("/permissions")
    @PreAuthorize("hasPermission(#form.chatUUID, @cpr.set())")
    public ChatUsersPermissionsDto setPermissions(@RequestBody @Valid SetPermissionsForm form){
        return chatService.setPermissions(form);
    }

}
