package com.example.library.management.controller;

import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        log.debug("Received create user request for email={}", userRequest.email());

        return userService.createUser(userRequest);
    }

    @DeleteMapping("/delete-user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Received delete user request for userId={}", userId);

        userService.deleteUser(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest loginDto) {
        log.debug("Received user login request for email={}", loginDto.email());

        return userService.getUser(loginDto.email(), loginDto.password());
    }

    @GetMapping("/get-all-users")
    public List<UserResponse> getAllUsers() {
        log.debug("Received get all users request");

        return userService.getAllUsers();
    }

}