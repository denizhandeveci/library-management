package com.example.library.management.service;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.dto.login.LoginResponse;
import com.example.library.management.entity.Admin;
import com.example.library.management.repository.AdminRepository;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService
{
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    @Autowired
    public AdminService(AdminRepository adminRepository, JwtService jwtService)
    {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse<AdminResponse> loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        if (!admin.password.equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        AdminResponse adminResponse = AdminResponse.fromEntity(admin);
        String token = jwtService.createToken(admin.id, admin.email, UserRole.ADMIN);

        return LoginResponse.bearer(token, adminResponse);
    }

    public AdminResponse createAdmin(AdminRequest adminRequest) {
        log.info("Creating admin with email={}", adminRequest.email());

        if (adminRepository.existsByEmail(adminRequest.email())) {
            log.warn("Admin creation rejected because email={} already exists", adminRequest.email());

            // TODO interceptor or graceful handling, yb
            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }

        Admin savedAdmin = adminRepository.save(adminRequest.toEntity());

        log.info("Admin created successfully with adminId={} and email={}", savedAdmin.id, savedAdmin.email);

        return AdminResponse.fromEntity(savedAdmin);
    }
}
