package com.aldevs.chatsplatform.controller;

import com.aldevs.chatsplatform.forms.*;
import com.aldevs.chatsplatform.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

}
