package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.ResponseUser;
import com.aldevs.chatsplatform.forms.RegistrationUser;
import com.aldevs.chatsplatform.resource.messageErrorResponse;
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
    public ResponseUser registration(@Valid @RequestBody RegistrationUser saveUser){
         return new ResponseUser(userService.saveUser(saveUser));
    }

    @ExceptionHandler
    public ResponseEntity<messageErrorResponse> handleAuthException(RuntimeException exception) {
        return new ResponseEntity<>(new messageErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
