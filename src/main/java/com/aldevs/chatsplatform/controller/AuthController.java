package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.AuthenticatedUser;
import com.aldevs.chatsplatform.forms.AuthenticationUser;
import com.aldevs.chatsplatform.forms.ResponseRegisterUser;
import com.aldevs.chatsplatform.forms.RegistrationUser;
import com.aldevs.chatsplatform.resource.messageErrorResponse;
import com.aldevs.chatsplatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseRegisterUser registration(@Valid @RequestBody RegistrationUser saveUser){
         return new ResponseRegisterUser(userService.saveUser(saveUser));
    }

    @PostMapping("/login")
    public AuthenticatedUser login(@Valid @RequestBody AuthenticationUser authenticationUser){
        return userService.loginUser(authenticationUser);
    }

    @ExceptionHandler
    public ResponseEntity<messageErrorResponse> handleAuthException(RuntimeException exception) {
        log.info("[AuthController] Caught AuthenticationException: " + exception.getMessage());
        return new ResponseEntity<>(new messageErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
