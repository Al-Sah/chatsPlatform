package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.Dtos.ChatTextMessageDto;
import com.aldevs.chatsplatform.forms.chat.*;
import com.aldevs.chatsplatform.service.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessageService messageService;

    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    @PreAuthorize("hasPermission(#message, @p.create())")
    public ChatTextMessageDto saveTextMessage(@AuthenticationPrincipal UserDetails info, @RequestBody @Valid ChatTextMessageRequest message){
        return messageService.sendMessage(info, message);
    }

    @PutMapping("/")
    @PreAuthorize("hasPermission(#message, @p.update())")
    public ChatTextMessageDto editTextMessage(@RequestBody @Valid EditTextChatMessage message){
        return messageService.editMessage(message);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasPermission(#message, @p.delete())")
    public ChatTextMessageDto deleteTextMessage(@RequestBody @Valid DeleteTextMessage message){
        return messageService.deleteMessage(message);
    }

    @GetMapping("/{chat}/{number}")
    @PreAuthorize("hasPermission(#chat, @p.read())")
    public List<ChatTextMessageDto> getLastMessages(@PathVariable String number, @PathVariable String chat){
        return messageService.getLastMessages(chat, number);
    }

    @GetMapping("/{chat}")
    @PreAuthorize("hasPermission(#chat, @p.read())")
    public List<ChatTextMessageDto> getAllMessages(@PathVariable String chat){
        return messageService.getAllMessages(chat);
    }
}
