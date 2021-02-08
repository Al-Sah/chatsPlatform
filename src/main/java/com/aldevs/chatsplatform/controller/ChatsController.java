package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatDto;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    private final ChatService chatService;

    public ChatsController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/local")
    public ChatCreationResponse createLocalChat(@AuthenticationPrincipal UserDetails info, @RequestParam String participant){
        return chatService.createChat(info, participant);
    }

    @GetMapping("/{chat}")
    public ChatDto viewChat(@PathVariable("chat") String chatUUID){
        //return chatService.findChatByUUID(chatUUID);
        return null;
    }

    @GetMapping("/list")
    public List<ChatDto> getPublicGroups(){
        return chatService.getPublicGroups();
    }

    @PostMapping("/join/{chat}")
    public ChatDto joinPublicGroup(@AuthenticationPrincipal UserDetails info, @PathVariable("chat") String chatUUID){
        return chatService.getPublicGroup(info, chatUUID);
    }


    @PostMapping("/join/")
    public ChatDto joinPrivateGroup(@AuthenticationPrincipal UserDetails info, @RequestParam("chat") String chatUUID, @RequestParam("pswd") String pswd){
        return chatService.getPrivateGroup(info, chatUUID, pswd);
    }

}
