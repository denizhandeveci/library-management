package com.example.library.management.dto;

import com.example.library.management.entity.Admin;

public record AdminRequest(
        String name,
        String email,
        String phoneNumber,
        String address,
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
