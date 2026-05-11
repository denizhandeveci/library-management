package com.example.library.management.dto;

import com.example.library.management.entity.User;

public record UserResponse(
        Long id,

        String name,
        String email,
        String phoneNumber,
        String address
)
{
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.id,

                user.name,
                user.email,
                user.phoneNumber,
                user.address
        );
    }
}