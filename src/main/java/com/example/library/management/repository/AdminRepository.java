package com.example.library.management.repository;

import com.example.library.management.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>
{
    @Query("""
            SELECT COUNT(a) > 0
            FROM Admin a
            WHERE a.email = :email
                AND a.deleted is NULL
            """)
    boolean existsByEmail(@Param("email") String email);

    @Query("""
            SELECT a
            FROM Admin a
            WHERE a.email = :email
            AND a.deleted IS NULL
            """)
    Optional<Admin> findByEmail(
            @Param("email") String email
    );
}
