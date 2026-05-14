package com.example.library.management.dto;

import com.example.library.management.entity.Admin;
import jakarta.validation.constraints.NotBlank;

public record AdminRequest(
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
    public Admin toEntity() {
        Admin admin = new Admin();

        admin.name = name();
        admin.email = email();
        admin.phoneNumber = phoneNumber();
        admin.address = address();
        admin.password = password();

        return admin;
    }
}
