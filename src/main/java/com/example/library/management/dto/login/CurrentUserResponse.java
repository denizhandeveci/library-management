package com.example.library.management.dto.login;

import com.example.library.management.security.UserRole;

public record CurrentUserResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {}
