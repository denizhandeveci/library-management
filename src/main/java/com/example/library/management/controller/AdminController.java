package com.example.library.management.controller;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController
{
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admins")
    public AdminResponse createAdmin(@RequestBody AdminRequest adminRequest) {
        log.debug("Received create admin request for email={}", adminRequest.email());

        return adminService.createAdmin(adminRequest);
    }

    @PostMapping("/admins/login")
    public ResponseEntity<AdminResponse> loginAdmin(@RequestBody AdminRequest loginDto) {
        log.debug("Received admin login request for email={}", loginDto.email());

        return adminService.getAdmin(loginDto.email(), loginDto.password());
    }
}
