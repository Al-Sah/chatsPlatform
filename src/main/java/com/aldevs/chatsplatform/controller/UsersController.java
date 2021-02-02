package com.aldevs.chatsplatform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @GetMapping("/{user}")
    public Object getUserProfile(@PathVariable String user){
        return null;
    }

    @GetMapping("/")
    public Object findUserBy(Object form){
        return null;
    }

    @GetMapping("/list")
    public Object getUsersList(){
        return null;
    }




}
