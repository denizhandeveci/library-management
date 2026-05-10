package com.example.library.management.service;

import com.example.library.management.dto.UserRequestDTO;
import com.example.library.management.dto.UserResponseDTO;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BookRepository bookRepository,
                       LoanRepository loanRepository,
                       ReviewRepository reviewRepository){
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reviewRepository = reviewRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }
        UserEntity userEntity = mapToEntity(userRequestDTO);
        UserEntity savedUser = userRepository.save(userEntity);

        return mapToDTO(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userRepository.delete(userEntity);
        reviewRepository.deleteByUserId(userId);


    }

    public ResponseEntity<UserResponseDTO> getUser(String email, String password){

        if(userRepository.findByEmailAndPassword(email, password).isPresent()){
            return ResponseEntity.ok(mapToDTO(userRepository.findByEmailAndPassword(email, password).get()));
        };

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserEntity mapToEntity(UserRequestDTO userRequestDTO){
        UserEntity userEntity = new UserEntity();

        userEntity.setName(userRequestDTO.getName());
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setPhoneNumber(userRequestDTO.getPhoneNumber());
        userEntity.setAddress(userRequestDTO.getAddress());
        userEntity.setPassword(userRequestDTO.getPassword());

        return userEntity;
    }

    public UserResponseDTO mapToDTO(UserEntity userEntity){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setName(userEntity.getName());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setAddress(userEntity.getAddress());
        userResponseDTO.setPhoneNumber(userEntity.getPhoneNumber());

        return userResponseDTO;
    }

}
