package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.service.ChatService;
import com.aldevs.chatsplatform.service.EditTextChatMessage;
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

    @PostMapping("/")
    public ChatTextMessageDto saveTextMessage(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody @Valid ChatTextMessageRequest message){
        return chatService.sendMessage(userDetails, message);
    }
    @PutMapping("/")
    public ChatTextMessageDto editTextMessage(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody @Valid EditTextChatMessage message){
        return chatService.editMessage(userDetails, message);
    }

    @PostMapping("/new")
    public ChatCreationResponse createChat(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam String participant){
        return chatService.createChat(userDetails, participant);
    }

    @GetMapping("/{chat}/{number}")
    public List<ChatTextMessageDto> getLastMessages(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable String number, @PathVariable String chat){
        return chatService.getLastMessages(userDetails, chat, number);
    }

    @GetMapping("/{chat}")
    public List<ChatTextMessageDto> getAllMessages(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable String chat){
        return chatService.getAllMessages(userDetails, chat);
    }


}
