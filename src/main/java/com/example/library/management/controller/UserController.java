package com.example.library.management.controller;

import com.example.library.management.entity.UserEntity;
import com.example.library.management.service.UserService;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete-user/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }

//    @PostMapping("/return-book/{userId}/{bookId}")
//    public void returnBook(@PathVariable Long userId, @PathVariable Long bookId){
//        userService.returnBook(userId,bookId);
//    }
}
