package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.forms.chat.ChatCreationResponse;
import com.aldevs.chatsplatform.forms.chat.ChatTextMessageRequest;
import com.aldevs.chatsplatform.forms.chat.DeleteTextMessage;
import com.aldevs.chatsplatform.service.ChatService;
import com.aldevs.chatsplatform.forms.chat.EditTextChatMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user/chats")
public class ChatsController {

    private final ChatService chatService;

    public ChatsController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/")
    @PreAuthorize("hasPermission(#message, @p.create())")
    public ChatTextMessageDto saveTextMessage(@AuthenticationPrincipal UserDetails info, @RequestBody @Valid ChatTextMessageRequest message){
        return chatService.sendMessage(info, message);
    }


    @PutMapping("/")
    @PreAuthorize("hasPermission(#message, @p.update())")
    public ChatTextMessageDto editTextMessage(@RequestBody @Valid EditTextChatMessage message){
        return chatService.editMessage(message);
    }


    @DeleteMapping("/")
    @PreAuthorize("hasPermission(#message, @p.delete())")
    public ChatTextMessageDto deleteTextMessage(@RequestBody @Valid DeleteTextMessage message){
        return chatService.deleteMessage(message);
    }

    @GetMapping("/{chat}/{number}")
    @PreAuthorize("hasPermission(#chat, @p.read())")
    public List<ChatTextMessageDto> getLastMessages(@PathVariable String number, @PathVariable String chat){
        return chatService.getLastMessages(chat, number);
    }

    @GetMapping("/{chat}")
    @PreAuthorize("hasPermission(#chat, @p.read())")
    public List<ChatTextMessageDto> getAllMessages(@PathVariable String chat){
        return chatService.getAllMessages(chat);
    }



    @PostMapping("/new")
    public ChatCreationResponse createChat(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam String participant){
        return chatService.createChat(userDetails, participant);
    }

}
