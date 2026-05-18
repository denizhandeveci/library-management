package com.example.library.management.repository;

import com.example.library.management.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE reviews AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

    List<Review> findByBookId(Long bookId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.deleted is NULL")
    List<Review> findByUserId(Long userId);
}
