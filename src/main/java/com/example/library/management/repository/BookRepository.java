package com.example.library.management.repository;

import com.example.library.management.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    @Modifying
    @Query(value = "ALTER TABLE book_entity AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
