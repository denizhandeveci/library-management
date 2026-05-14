package com.example.library.management.dto.login;

public record LoginResponse<T>(
        // TODO remove the token from response once the cookie-based setup is stable   ,yb
        //  keep for debugging for now
        String token,
        String tokenType,
        T user
)
{
    public static <T> LoginResponse<T> bearer(String token, T user) {
        return new LoginResponse<>(token, "Bearer", user);
    }
}
