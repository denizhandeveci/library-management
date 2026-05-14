package com.example.library.management.service;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.dto.login.LoginResponse;
import com.example.library.management.entity.Admin;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ConflictException;
import com.example.library.management.repository.AdminRepository;
import com.example.library.management.repository.UserRepository;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            AdminRepository adminRepository,
            JwtService jwtService
    )
    {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists.");
        }

        User savedUser = userRepository.save(request.toEntity());

        return UserResponse.fromEntity(savedUser);
    }

    public LoginResponse<UserResponse> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(this::invalidCredentials);

        if (!user.password.equals(password)) {
            throw invalidCredentials();
        }

        UserResponse response = UserResponse.fromEntity(user);
        String token = jwtService.createToken(user.id, user.email, UserRole.USER);

        return LoginResponse.bearer(token, response);
    }

    public AdminResponse registerAdmin(AdminRequest request) {
        if (adminRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists.");
        }

        Admin savedAdmin = adminRepository.save(request.toEntity());

        return AdminResponse.fromEntity(savedAdmin);
    }

    public LoginResponse<AdminResponse> loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(this::invalidCredentials);

        if (!admin.password.equals(password)) {
            throw invalidCredentials();
        }

        AdminResponse response = AdminResponse.fromEntity(admin);
        String token = jwtService.createToken(admin.id, admin.email, UserRole.ADMIN);

        return LoginResponse.bearer(token, response);
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }
}
