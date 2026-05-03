package com.example.library.management.repository;

import com.example.library.management.entity.AdminEntity;
import com.example.library.management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    boolean existsByEmail(String email);
    Optional<AdminEntity> findByEmailAndPassword(String email, String password);

}
