package com.example.library.management.controller;

import com.example.library.management.dto.UserResponse;
import com.example.library.management.security.AccessEnforcer;
import com.example.library.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AccessEnforcer accessEnforcer;

    public UserController(UserService userService, AccessEnforcer accessEnforcer) {
        this.userService = userService;
        this.accessEnforcer = accessEnforcer;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        accessEnforcer.requireAdmin();

        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        accessEnforcer.requireAdmin();

        log.debug("Received delete user request for userId={}", userId);

        userService.deleteUser(userId);
    }

}