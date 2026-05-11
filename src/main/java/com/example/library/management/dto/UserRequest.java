package com.example.library.management.dto;

import com.example.library.management.entity.User;

public record UserRequest(
        String name,
        String email,
        String phoneNumber,
        String address,
        String password
)
{
    public User toEntity() {
        User user = new User();

        user.name = name();
        user.email = email();
        user.phoneNumber = phoneNumber();
        user.address = address();
        user.password = password();

        return user;
    }
}
