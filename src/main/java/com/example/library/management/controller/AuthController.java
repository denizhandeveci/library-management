package com.example.library.management.controller;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.dto.login.CurrentUserResponse;
import com.example.library.management.security.AccessEnforcer;
import com.example.library.management.security.JwtCookieService;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import com.example.library.management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
    private final AuthService authService;
    private final JwtCookieService jwtCookieService;
    private final JwtService jwtService;
    private final AccessEnforcer accessEnforcer;

    public AuthController(
            AuthService authService,
            JwtCookieService jwtCookieService,
            JwtService jwtService,
            AccessEnforcer accessEnforcer
    )
    {
        this.authService = authService;
        this.jwtCookieService = jwtCookieService;
        this.jwtService = jwtService;
        this.accessEnforcer = accessEnforcer;
    }

    @PostMapping("/auth/users/register")
    public UserResponse registerUser(@RequestBody @Valid UserRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/auth/users/login")
    public ResponseEntity<CurrentUserResponse> loginUser(@RequestBody UserRequest request) {
        var authResult = authService.loginUser(request.email(), request.password());

        ResponseCookie cookie = jwtCookieService.createAuthCookie(
                authResult.token(),
                jwtService.getExpirationMinutes()
        );

        UserResponse user = authResult.user();
        var currentUser = new CurrentUserResponse(
                user.id(),
                user.name(),
                user.email(),
                UserRole.USER
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(currentUser);
    }

    @PostMapping("/auth/admins/register")
    public AdminResponse registerAdmin(@RequestBody @Valid AdminRequest request) {
        return authService.registerAdmin(request);
    }

    @PostMapping("/auth/admins/login")
    public ResponseEntity<CurrentUserResponse> loginAdmin(@RequestBody AdminRequest request) {
        var response = authService.loginAdmin(request.email(), request.password());

        ResponseCookie cookie = jwtCookieService.createAuthCookie(response.token(), jwtService.getExpirationMinutes());

        AdminResponse admin = response.user();
        var currentUser = new CurrentUserResponse(
                admin.id(),
                admin.name(),
                admin.email(),
                UserRole.ADMIN
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(currentUser);
    }

    @GetMapping("/auth/me")
    public CurrentUserResponse me() {
        var currentUser = accessEnforcer.getCurrentUser();

        return new CurrentUserResponse(
                currentUser.id(),
                null,
                currentUser.email(),
                currentUser.role()
        );
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = jwtCookieService.clearAuthCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
