package com.example.library.management.controller;

import com.example.library.management.dto.AdminRequestDTO;
import com.example.library.management.dto.AdminResponseDTO;
import com.example.library.management.dto.UserRequestDTO;
import com.example.library.management.dto.UserResponseDTO;
import com.example.library.management.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private final com.example.library.management.service.AdminService AdminService;
    private final AdminService adminService;

    public AdminController(AdminService AdminService, AdminService adminService) {
        this.AdminService = AdminService;
        this.adminService = adminService;
    }
    @PostMapping("/create-admin")
    public AdminResponseDTO createAdmin(@RequestBody AdminRequestDTO AdminRequestDTO){
        return AdminService.createAdmin(AdminRequestDTO);
    }

    @PostMapping("/admin-login")
    public ResponseEntity<AdminResponseDTO> loginAdmin(@RequestBody AdminRequestDTO loginDto) {
        return adminService.getAdmin(loginDto.getEmail(), loginDto.getPassword());
    }


//    @PostMapping("/return-book/{AdminId}/{bookId}")
//    public void returnBook(@PathVariable Long AdminId, @PathVariable Long bookId){
//        AdminService.returnBook(AdminId,bookId);
//    }
}
