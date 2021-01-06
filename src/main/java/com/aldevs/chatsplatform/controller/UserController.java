package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.ResponseUser;
import com.aldevs.chatsplatform.forms.SaveUser;
import com.aldevs.chatsplatform.resoursses.messageErrorResponse;
import com.aldevs.chatsplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseUser registration(@Valid @RequestBody SaveUser saveUser){
         return userService.formResponse(userService.saveUser(saveUser));
    }

    @ExceptionHandler
    public ResponseEntity<messageErrorResponse> handleAuthException(RuntimeException exception) {
        var response = new messageErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
