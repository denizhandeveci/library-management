package com.example.library.management.dto;

import com.example.library.management.entity.Admin;

public record AdminResponse(
        Long id,

        String name,
        String email,
        String phoneNumber,
        String address
)
{
    public static AdminResponse fromEntity(Admin admin) {
        return new AdminResponse(
                admin.id,

                admin.name,
                admin.email,
                admin.phoneNumber,
                admin.address
        );
    }
}
