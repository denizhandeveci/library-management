package com.example.library.management.dto.login;

public record AuthResult<T>(
        String token,
        T user
) {}
