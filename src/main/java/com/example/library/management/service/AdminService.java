package com.example.library.management.service;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.entity.Admin;
import com.example.library.management.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService
{
    private final com.example.library.management.repository.AdminRepository AdminRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(
            AdminRepository AdminRepository,
            AdminRepository adminRepository
    )
    {
        this.AdminRepository = AdminRepository;
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<AdminResponse> getAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmailAndPassword(email, password).orElse(null);
        if (admin != null) {
            return ResponseEntity.ok(AdminResponse.fromEntity(admin));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public AdminResponse createAdmin(AdminRequest adminRequest) {
        if (AdminRepository.existsByEmail(adminRequest.email())) {
            // TODO interceptor or graceful handling, yb
            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }

        Admin savedAdmin = AdminRepository.save(adminRequest.toEntity());

        return AdminResponse.fromEntity(savedAdmin);
    }
}
