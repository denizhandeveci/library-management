package com.example.library.management.repository;

import com.example.library.management.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>
{
    boolean existsByEmail(String email);

    Optional<Admin> findByEmailAndPassword(String email, String password);

}
