package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    private final ChatService chatService;

    public ChatsController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/")
    public Object saveMessage(Object message){
        return null;
    }

    @PostMapping("/new")
    public ChatCreationResponse createChat(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String participant){
        return chatService.createChat(userDetails, participant);
    }


}
