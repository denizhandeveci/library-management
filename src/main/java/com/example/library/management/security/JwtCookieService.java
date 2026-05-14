package com.example.library.management.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
public class JwtCookieService
{
    private final JwtCookieProperties properties;

    public JwtCookieService(JwtCookieProperties properties) {
        this.properties = properties;
    }

    public ResponseCookie createAuthCookie(String token, long expirationMinutes) {
        return ResponseCookie.from(properties.name(), token)
                .httpOnly(true)
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .path("/")
                .maxAge(Duration.ofMinutes(expirationMinutes))
                .build();
    }

    public ResponseCookie clearAuthCookie() {
        return ResponseCookie.from(properties.name(), "")
                .httpOnly(true)
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }

    public Optional<String> extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> properties.name().equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
