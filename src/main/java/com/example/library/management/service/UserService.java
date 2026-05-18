package com.example.library.management.service;

import com.example.library.management.dto.UserResponse;
import com.example.library.management.entity.BaseEntity;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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

    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();

        log.debug("Found {} users", users.size());

        return users;
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Soft deleting user with userId={}", userId);

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User deletion failed because userId={} was not found", userId);
                    return ResourceNotFoundException.forId("User", userId);
                });

        userEntity.softDelete();

        var reviews = reviewRepository.findByUserId(userId);
        reviews.forEach(BaseEntity::softDelete);

        log.info(
                "User soft deleted successfully with userId={}. Soft deleted {} related reviews",
                userId,
                reviews.size()
        );
    }
}