package com.example.library.management.service;

import com.example.library.management.dto.UserRequest;
import com.example.library.management.dto.UserResponse;
import com.example.library.management.dto.login.LoginResponse;
import com.example.library.management.entity.BaseEntity;
import com.example.library.management.entity.User;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService
{
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            JwtService jwtService
    )
    {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.jwtService = jwtService;
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user with email={}", userRequest.email());

        if (userRepository.existsByEmail(userRequest.email())) {
            log.warn("User creation rejected because email={} already exists", userRequest.email());

            throw new RuntimeException("This email address has already been used. Please use another email address.");
        }
        User savedUser = userRepository.save(userRequest.toEntity());

        log.info("User created successfully with userId={} and email={}", savedUser.id, savedUser.email);

        return UserResponse.fromEntity(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Soft deleting user with userId={}", userId);

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User deletion failed because userId={} was not found", userId);
                    return new RuntimeException("User not found");
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

    public LoginResponse<UserResponse> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        if (!user.password.equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        UserResponse userResponse = UserResponse.fromEntity(user);
        String token = jwtService.createToken(user.id, user.email, UserRole.USER);

        log.warn("User login failed for email={}", email);

        return LoginResponse.bearer(token, userResponse);
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();

        log.debug("Found {} users", users.size());

        return users;
    }
}