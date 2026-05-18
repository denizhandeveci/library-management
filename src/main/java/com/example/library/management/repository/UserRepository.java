package com.example.library.management.repository;

import com.example.library.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    @Query("""
            SELECT COUNT(u) > 0
            FROM User u
            WHERE u.email = :email
                AND u.deleted is NULL
            """)
    boolean existsByEmail(@Param("email") String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.email = :email
            AND u.password = :password
            AND u.deleted IS NULL
            """)
    Optional<User> findByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );

    @Query("""
            SELECT u
            FROM User u
            WHERE u.email = :email
            AND u.deleted IS NULL
            """)
    Optional<User> findByEmail(
            @Param("email") String email
    );
}
