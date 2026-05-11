package com.example.library.management.service;

import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.entity.BaseEntity;
import com.example.library.management.entity.User;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            ReviewRepository reviewRepository
    )
    {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }
        User savedUser = userRepository.save(userRequest.toEntity());

        return UserResponse.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User userEntity = userRepository.findById(userId).orElseThrow();

        userEntity.softDelete();

        var reviews = reviewRepository.findByUserId(userId);
        reviews.forEach(BaseEntity::softDelete);
    }

    public ResponseEntity<UserResponse> getUser(String email, String password) {

        User user = userRepository.findByEmailAndPassword(email, password).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(UserResponse.fromEntity(user));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}