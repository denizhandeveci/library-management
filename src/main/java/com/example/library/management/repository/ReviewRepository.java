package com.example.library.management.repository;

import com.example.library.management.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>
{
    List<Review> findByBookId(Long bookId);

    @Modifying
    @Query(value = "ALTER TABLE review_entity AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
