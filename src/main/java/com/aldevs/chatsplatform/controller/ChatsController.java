package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.forms.chat.CreationLocalChat;
import com.aldevs.chatsplatform.forms.chat.GroupCreationForm;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/chats")
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
    //@PreAuthorize("hasPermission(#form, @cpr.create())")
    public ChatDto createGroup(@AuthenticationPrincipal UserDetails info, @Valid @RequestBody GroupCreationForm form){
        return chatService.createGroup(info, form);
    }

    @GetMapping("/{chat}")
    public ChatDto viewChat(@AuthenticationPrincipal UserDetails info, @PathVariable("chat") String chatUUID){
        return chatService.viewChat(info, chatUUID);
    }

    @GetMapping("/list")
    public List<ChatDto> getPublicGroups(){
        return chatService.getPublicGroups();
    }

    @PostMapping("/join/public")
    public ChatDto joinPublicGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID){
        return chatService.joinPublicGroup(info, chatUUID);
    }

    @PostMapping("/join/private")
    public ChatDto joinPrivateGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID, @RequestParam("pswd") String pswd){
        return chatService.joinPrivateGroup(info, chatUUID, pswd);
    }

}
