package com.example.library.management.repository;

import com.example.library.management.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    @Modifying
    @Query(value = "ALTER TABLE book_entity AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
    Optional<BookEntity> findByTitle(String title);
    List<BookEntity> findByGenre(String genre);
    List<BookEntity> findByAuthor(String author);
}
