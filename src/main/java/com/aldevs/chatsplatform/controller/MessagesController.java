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
    @PreAuthorize("hasPermission(#message, @mpr.create())")
    public ChatTextMessageDto saveTextMessage(@AuthenticationPrincipal UserDetails info, @RequestBody @Valid ChatTextMessageRequest message){
        return messageService.sendMessage(info, message);
    }

    @PutMapping("/")
    @PreAuthorize("hasPermission(#message, @mpr.update())")
    public ChatTextMessageDto editTextMessage(@RequestBody @Valid EditTextChatMessage message){
        return messageService.editMessage(message);
    }

    @PutMapping("/recover")
    @PreAuthorize("hasPermission(#message, @mpr.delete())")
    public ChatTextMessageDto recoverTextMessage(@RequestBody @Valid DeleteTextMessage message){
        return messageService.recoverMessage(message);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasPermission(#message, @mpr.delete())")
    public ChatTextMessageDto deleteTextMessage(@RequestBody @Valid DeleteTextMessage message){
        return messageService.deleteMessage(message);
    }


    @GetMapping("/{chat}/20")
    @PreAuthorize("hasPermission(#chat, @mpr.read())")
    public List<ChatTextMessageDto> getLastMessages(@PathVariable String chat){
        return messageService.getLast20Messages(chat);
    }

    @GetMapping("/{chat}/page/{page}")
    @PreAuthorize("hasPermission(#chat, @mpr.read())")
    public List<ChatTextMessageDto> getMessagesPage(@PathVariable String chat, @PathVariable String page){
        return messageService.getMessagesPage(chat, page);
    }

    @GetMapping("/{chat}")
    @PreAuthorize("hasPermission(#chat, @mpr.read())")
    public List<ChatTextMessageDto> getAllMessages(@PathVariable String chat){
        return messageService.getAllMessages(chat);
    }
}
