package com.security.spring_security.controller;

import com.security.spring_security.entity.User;
import com.security.spring_security.repository.UserRepository;
import com.security.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController

public class UserController {

    @Autowired
    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User saveUser(@RequestBody User user){
        return  userService.register(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user){
        return userService.verify(user);

    }
}
