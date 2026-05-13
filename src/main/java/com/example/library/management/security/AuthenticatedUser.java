package com.example.library.management.security;

public record AuthenticatedUser(
        Long id,
        String email,
        UserRole role
) {}
