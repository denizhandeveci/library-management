package com.example.library.management.controller;

import com.example.library.management.dto.AdminRequest;
import com.example.library.management.dto.AdminResponse;
import com.example.library.management.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController
{

    private final com.example.library.management.service.AdminService AdminService;
    private final AdminService adminService;

    public AdminController(AdminService AdminService, AdminService adminService) {
        this.AdminService = AdminService;
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public ResponseEntity<AdminResponse> loginAdmin(@RequestBody AdminRequest loginDto) {
        return adminService.getAdmin(loginDto.email(), loginDto.password());
    }
    @PostMapping("/admins")
    public AdminResponse createAdmin(@RequestBody AdminRequest AdminRequestDTO) {
        return AdminService.createAdmin(AdminRequestDTO);
    }
}
