package com.example.library.management.controller;

import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public UserResponse createUser(@RequestBody UserRequest userRequestDTO) {
        return userService.createUser(userRequestDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest loginDto) {
        return userService.getUser(loginDto.email(), loginDto.password());
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }



}