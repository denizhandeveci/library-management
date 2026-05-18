package com.example.library.management.service;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.dto.login.AuthResult;
import com.example.library.management.entity.Admin;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ConflictException;
import com.example.library.management.repository.AdminRepository;
import com.example.library.management.repository.UserRepository;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            AdminRepository adminRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    )
    {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists.");
        }

        User user = request.toEntity();
        user.passwordHash = passwordEncoder.encode(request.password());

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    public AuthResult<UserResponse> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw invalidCredentials();
        }

        UserResponse response = UserResponse.fromEntity(user);
        String token = jwtService.createToken(user.id, user.email, UserRole.USER);

        return new AuthResult<>(token, response);
    }

    public AdminResponse registerAdmin(AdminRequest request) {
        if (adminRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists.");
        }

        Admin admin = request.toEntity();
        admin.passwordHash = passwordEncoder.encode(request.password());

        Admin savedAdmin = adminRepository.save(admin);

        return AdminResponse.fromEntity(savedAdmin);
    }

    public AuthResult<AdminResponse> loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(password, admin.passwordHash)) {
            throw invalidCredentials();
        }

        AdminResponse response = AdminResponse.fromEntity(admin);
        String token = jwtService.createToken(admin.id, admin.email, UserRole.ADMIN);

        return new AuthResult<>(token, response);
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }
}
