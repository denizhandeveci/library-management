package com.example.library.management.service;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.entity.Admin;
import com.example.library.management.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService
{
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository)
    {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<AdminResponse> getAdmin(String email, String password) {
        log.info("Admin login attempt for email={}", email);

        Admin admin = adminRepository.findByEmailAndPassword(email, password).orElse(null);
        if (admin != null) {
            log.info("Admin login successful for adminId={} and email={}", admin.id, email);

            return ResponseEntity.ok(AdminResponse.fromEntity(admin));
        }

        log.warn("Admin login failed for email={}", email);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
