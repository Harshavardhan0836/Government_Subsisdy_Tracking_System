package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.User;
import com.example.Subsidy_Tracking_System.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/User/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @PostMapping("/User/login")
    public String login(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        return token != null ? token : "Invalid credentials";
    }
}