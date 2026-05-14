package com.example.library.management.controller;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.dto.login.LoginResponse;
import com.example.library.management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/users/register")
    public UserResponse registerUser(@RequestBody @Valid UserRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/auth/users/login")
    public LoginResponse<UserResponse> loginUser(@RequestBody UserRequest request) {
        return authService.loginUser(request.email(), request.password());
    }

    @PostMapping("/auth/admins/register")
    public AdminResponse registerAdmin(@RequestBody @Valid AdminRequest request) {
        return authService.registerAdmin(request);
    }

    @PostMapping("/auth/admins/login")
    public LoginResponse<AdminResponse> loginAdmin(@RequestBody AdminRequest request) {
        return authService.loginAdmin(request.email(), request.password());
    }
}
