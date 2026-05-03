package com.example.library.management.service;

import com.example.library.management.dto.AdminRequestDTO;
import com.example.library.management.dto.AdminResponseDTO;
import com.example.library.management.dto.UserResponseDTO;
import com.example.library.management.entity.AdminEntity;
import com.example.library.management.repository.AdminRepository;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final com.example.library.management.repository.AdminRepository AdminRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReviewRepository reviewRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository AdminRepository,
                        BookRepository bookRepository,
                        LoanRepository loanRepository,
                        ReviewRepository reviewRepository, AdminRepository adminRepository){
        this.AdminRepository = AdminRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reviewRepository = reviewRepository;
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<AdminResponseDTO> getAdmin(String email, String password){
        if(adminRepository.findByEmailAndPassword(email, password).isPresent()){
            return ResponseEntity.ok(mapToDTO(adminRepository.findByEmailAndPassword(email, password).get()));
        };
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public AdminResponseDTO createAdmin(AdminRequestDTO AdminRequestDTO){
        if(AdminRepository.existsByEmail(AdminRequestDTO.getEmail())){
            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }
        AdminEntity AdminEntity = mapToEntity(AdminRequestDTO);
        AdminEntity savedAdmin = AdminRepository.save(AdminEntity);

        return mapToDTO(savedAdmin);
    }
    public AdminEntity mapToEntity(AdminRequestDTO AdminRequestDTO){
        AdminEntity AdminEntity = new AdminEntity();

        AdminEntity.setName(AdminRequestDTO.getName());
        AdminEntity.setEmail(AdminRequestDTO.getEmail());
        AdminEntity.setPhoneNumber(AdminRequestDTO.getPhoneNumber());
        AdminEntity.setAddress(AdminRequestDTO.getAddress());
        AdminEntity.setPassword(AdminRequestDTO.getPassword());

        return AdminEntity;
    }

    public AdminResponseDTO mapToDTO(AdminEntity AdminEntity){
        AdminResponseDTO AdminResponseDTO = new AdminResponseDTO();

        AdminResponseDTO.setId(AdminEntity.getId());
        AdminResponseDTO.setName(AdminEntity.getName());
        AdminResponseDTO.setEmail(AdminEntity.getEmail());
        AdminResponseDTO.setAddress(AdminEntity.getAddress());
        AdminResponseDTO.setPhoneNumber(AdminEntity.getPhoneNumber());

        return AdminResponseDTO;
    }

}
