package com.example.library.management.dto;

import com.example.library.management.entity.User;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank
        String name,

        @NotBlank
        String email,

        String phoneNumber,
        String address,

        @NotBlank
        String password
)
{
    public User toEntity() {
        User user = new User();

        user.name = name();
        user.email = email();
        user.phoneNumber = phoneNumber();
        user.address = address();

        // password is hashed before save

        return user;
    }
}
