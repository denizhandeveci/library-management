package com.example.library.management.controller;

import com.example.library.management.entity.UserEntity;
import com.example.library.management.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/create-user")
    public UserEntity createUser(@RequestBody UserEntity userEntity){
        return userService.createUser(userEntity);
    }

    @PostMapping("/return-book/{userId}/{bookId}")
    public void returnBook(@PathVariable Long userId, @PathVariable Long bookId){
        userService.returnBook(userId,bookId);
    }
}
