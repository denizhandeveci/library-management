package com.example.library.management.dto.login;

public record LoginResponse<T>(
        String token,
        String tokenType,
        T user
)
{
    public static <T> LoginResponse<T> bearer(String token, T user) {
        return new LoginResponse<>(token, "Bearer", user);
    }
}
